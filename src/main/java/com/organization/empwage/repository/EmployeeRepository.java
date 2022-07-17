package com.organization.empwage.repository;

import com.organization.empwage.exception.ModelException;
import com.organization.empwage.exception.ValidationException;
import com.organization.empwage.helper.AppMessages;
import com.organization.empwage.model.Employee;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepository {
    private static final Map<Long, Employee> empMap = new HashMap<>();
    private static Long index = 0L;

    static {
        try {
            givenCSVFile_whenRead_thenContentsAsExpected();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void givenCSVFile_whenRead_thenContentsAsExpected() throws IOException, ParseException {
        String[] HEADERS = {"id", "surname", "name", "patronymic", "birthDate", "phoneNumber", "departmentCode", "salary"};
        File file = new File(FileSystems.getDefault().getPath("").toAbsolutePath().toFile().getAbsolutePath()+ "/src/main/resources/static/data.csv");
        if (file.exists()) {
            Reader in = new FileReader(file);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader(HEADERS)
                    .withFirstRecordAsHeader()
                    .parse(in);
            for (CSVRecord record : records) {
                Long id = Long.valueOf(record.get("id"));
                String surname = record.get("surname");
                String name = record.get("name");
                String patronymic = record.get("patronymic");
                Date birthDate = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(record.get("birthDate"));
                String phoneNumber = record.get("phoneNumber");
                Long departmentCode = Long.valueOf(record.get("departmentCode"));
                String salaryStr = record.get("salary").equals("") ? "0" : record.get("salary");
                BigDecimal salary = BigDecimal.valueOf(Long.parseLong(salaryStr));
                if (index < id) index = id;
                empMap.put(id, new Employee(
                        id,
                        surname,
                        name,
                        patronymic,
                        birthDate,
                        phoneNumber,
                        departmentCode,
                        salary
                ));
            }
        }
    }

    public Optional<Employee> findById(Long id) {
        if (!isHave(id)) throw new ValidationException(String.format("ID = %d %s", id, AppMessages.NOT_FOUND));
        return Optional.of(empMap.get(id));
    }

    public List<Employee> getAll() {
        return new ArrayList<>(empMap.values());
    }

    public Employee save(Employee employee) {
        employee.setId(++index);
        Employee employee1 = empMap.put(employee.getId(), employee);
        if (employee1 == null) {
            File file = new File(FileSystems.getDefault().getPath("").toAbsolutePath().toFile().getAbsolutePath()+ "/src/main/resources/static/data.csv");
            try {
                createCSVFile(file);
                return employee;
            } catch (IOException e) {
                e.printStackTrace();
                empMap.put(employee.getId(), employee);
                throw new ModelException("employee");
            }
        }
        empMap.put(employee1.getId(), employee1);
        throw new ModelException("employee");
    }


    public Employee update(Employee employee) {
        if (!isHave(employee.getId()))
            throw new ValidationException(String.format("ID = %d %s", employee.getId(), AppMessages.NOT_FOUND));
        Employee employee1 = empMap.put(employee.getId(), employee);
        File file = new File(FileSystems.getDefault().getPath("").toAbsolutePath().toFile().getAbsolutePath()+ "/src/main/resources/static/data.csv");
        try {
            createCSVFile(file);
            return employee;
        } catch (IOException e) {
            e.printStackTrace();
            empMap.put(employee1.getId(), employee1);
            throw new ModelException("employee");
        }
    }

    public Employee deleteById(Long id) {
        if (!isHave(id)) throw new ValidationException(String.format("ID = %d %s", id, AppMessages.NOT_FOUND));
        Employee employee = empMap.remove(id);
        File file = new File(FileSystems.getDefault().getPath("").toAbsolutePath().toFile().getAbsolutePath()+ "/src/main/resources/static/data.csv");
        try {
            createCSVFile(file);
            return employee;
        } catch (IOException e) {
            e.printStackTrace();
            empMap.put(employee.getId(), employee);
            throw new ModelException("employee");
        }
    }

    private boolean isHave(Long id) {
        return empMap.containsKey(id);
    }

    public void createCSVFile(File file) throws IOException {
        String[] HEADERS = {"id", "surname", "name", "patronymic", "birthDate", "phoneNumber", "departmentCode", "salary"};
        FileWriter out = new FileWriter(file);
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                .withHeader(HEADERS))) {
            empMap.values().stream().peek(employee -> {
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    String strDate = dateFormat.format(employee.getBirthDate());
                    printer.printRecord(
                            employee.getId(),
                            employee.getSurname(),
                            employee.getName(),
                            employee.getPatronymic(),
                            strDate,
                            employee.getPhoneNumber(),
                            employee.getDepartmentCode(),
                            employee.getSalary()
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).collect(Collectors.toList());
        }
    }
}
