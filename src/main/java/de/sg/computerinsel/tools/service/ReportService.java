package de.sg.computerinsel.tools.service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class ReportService {

    private static final String FILENAME_BEILAGE = "beilage.jasper";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public File createReportReparaturBeilage(final Reparatur reparatur) throws JRException, IOException, SQLException {
        final JasperReport report = (JasperReport) JRLoader.loadObjectFromFile(loadReport(FILENAME_BEILAGE).getAbsolutePath());

        final Map<String, Object> params = new HashMap<>();
        params.put("Parameter1", reparatur.getId());
        params.put("imagesDir", getImagesDir());

        return createReport(report, params);
    }

    private String getImagesDir() throws IOException {
        final String directory = new ClassPathResource("reports").getFile().getAbsolutePath();
        return directory.endsWith(File.separator) ? directory : directory + File.separator;
    }

    private File createReport(final JasperReport report, final Map<String, Object> params) throws SQLException, JRException, IOException {
        final Connection connection = jdbcTemplate.getDataSource().getConnection();
        final JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, connection);

        final File tempFile = File.createTempFile("Report_" + params.get("Parameter1") + "_", ".pdf");
        JasperExportManager.exportReportToPdfFile(jasperPrint, tempFile.getAbsolutePath());
        return tempFile;
    }

    private File loadReport(final String filename) throws IOException {
        return new ClassPathResource("reports" + File.separator + filename).getFile();
    }

}
