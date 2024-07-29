package com.example.neoxamBack.services;

import com.example.neoxamBack.entities.*;
import com.example.neoxamBack.repositories.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ExcelService {

    @Autowired
    private assigneeRepository assigneeRepository;

    @Autowired
    private moduleRepository moduleRepository;

    @Autowired
    private ticketrepository ticketRepository;

    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    AssigneeModuleStatusRepository assigneeModuleStatusRepository;


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm");


    private Date parseDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            System.err.println("Failed to parse date: " + dateString);
            e.printStackTrace();
            return null;
        }
    }

    public void saveExcelData(MultipartFile file, int version) throws IOException, ParseException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Row headerRow = sheet.getRow(0);

        // Map column names to indices
        Map<String, Integer> columnMap = new HashMap<>();
        for (Cell cell : headerRow) {
            columnMap.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (row != null) {
                String assigneeName = getCellValue(row, columnMap, "Assignee");
                String moduleFeature = getCellValue(row, columnMap, "Module - Feature");
                String priority = getCellValue(row, columnMap, "Priority");
                String status = getCellValue(row, columnMap, "Status");
                Date createdDate = parseDate(getCellValue(row, columnMap, "Created"));
                String summary = getCellValue(row, columnMap, "Summary");
                String resolution = getCellValue(row, columnMap, "Resolution");
                Date resolvedDate = parseDate(getCellValue(row, columnMap, "Resolved"));
                String reporter = getCellValue(row, columnMap, "Reporter");

                String[] moduleFeatureParts = moduleFeature.split(" - ");
                if (moduleFeatureParts.length < 3) {
                    System.err.println("Invalid module-feature format: " + moduleFeature);
                    continue;
                }
                String moduleName = moduleFeatureParts[1];
                String featureName = moduleFeatureParts[2];


                assignee assignee = assigneeRepository.findByName(assigneeName)
                        .orElseGet(() -> assigneeRepository.save(new assignee(assigneeName)));

                module module = moduleRepository.findByName(moduleName)
                        .orElseGet(() -> moduleRepository.save(new module(moduleName)));

                List<module> allModules = moduleRepository.findAll();
                for (module m : allModules) {
                    long ticketCount = ticketRepository.countByAssigneeIdAndFeatureModuleId(assignee.getId(), m.getId());


                        AssigneeModuleStatus assigneeModuleStatus = assigneeModuleStatusRepository
                                .findByAssigneeAndModule(assignee, m)
                                .orElse(new AssigneeModuleStatus());

                        assigneeModuleStatus.setAssignee(assignee);
                        assigneeModuleStatus.setModule(m);
                    if(m.getId() != module.getId()){
                        assigneeModuleStatus.setAssigneeStatus(ticketCount > 0 ? 2 : 0);
                    }else{
                        assigneeModuleStatus.setAssigneeStatus(2);
                    }
                    assigneeModuleStatusRepository.save(assigneeModuleStatus);
                }

                feature feature = featureRepository.findByNameAndModuleId(featureName, module.getId());
                if (feature == null) {
                    feature = new feature();
                    feature.setName(featureName);
                    feature.setModule(module);
                    feature = featureRepository.save(feature);
                }

                ticket ticket = new ticket();
                ticket.setAssignee(assignee);
                ticket.setFeature(feature);
                ticket.setPriority(priority);
                ticket.setStatus(status);
                ticket.setDate_created(createdDate);
                ticket.setSummary(summary);
                ticket.setResolution(resolution);
                ticket.setDate_resolved(resolvedDate);
                ticket.setReporter(reporter);
                ticket.setVersion(version);
                ticketRepository.save(ticket);


                System.out.println(ticket);
            }
        }
        workbook.close();
    }

    private String getCellValue(Row row, Map<String, Integer> columnMap, String columnName) {
        Integer colIndex = columnMap.get(columnName);
        if (colIndex != null) {
            Cell cell = row.getCell(colIndex);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case STRING:
                        return cell.getStringCellValue();
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            return dateFormat.format(cell.getDateCellValue());
                        } else {
                            return String.valueOf(cell.getNumericCellValue());
                        }
                    case BOOLEAN:
                        return String.valueOf(cell.getBooleanCellValue());
                    default:
                        return "";
                }
            }
        }
        return "";
    }
}
