package de.sg.computerinsel.tools;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author Sita Geßner
 */
public class ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    private static final SimpleDateFormat DATE_FORMAT_TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss");

    private static final String PREFIX_REPORT = ".pdf";

    private static final String REPORTS_BASE_PATH = "reports";

    private static final String REPORT_REPARATUR = "reparatur.jasper";

    private static final String REPORT_ABHOLBELEG = "abholbeleg.jasper";

    private static final String REPORT_AUFTRAGSUEBERSICHT = "auftragsuebersicht.jasper";

    private static final String SUFFIX_ABHOLBELEG = "abholbeleg";

    private static final String SUFFIX_AUFTRAGSUEBERSICHT = "auftragsuebersicht";

    private static final String SUFFIX_REPARATUR = "reparatur";

    private final Map<String, Object> connectionProps;

    public ReportService(final Map<String, Object> connectionProps) {
        this.connectionProps = connectionProps;
    }

    public File createReportAuftragsuebersicht(final Date datumVon, final Date datumBis, final Integer filialeId) {
        File report = null;
        final Map<String, Object> params = createReportParamsMap(datumVon, datumBis, filialeId);
        try (final Connection connection = getConnection()) {
            final JasperPrint jprint = JasperFillManager.fillReport(getReportFilePath(REPORT_AUFTRAGSUEBERSICHT).getAbsolutePath(), params,
                    connection);
            report = File.createTempFile(DATE_FORMAT_TIMESTAMP.format(new Date()) + SUFFIX_AUFTRAGSUEBERSICHT, PREFIX_REPORT);
            JasperExportManager.exportReportToPdfFile(jprint, report.getAbsolutePath());
        } catch (final JRException e) {
            LOGGER.error("Fehler beim Laden der Berichte: {} {}", e.getMessage(), e);
        } catch (final IOException e) {
            LOGGER.error("Fehler beim Schreiben des Berichtes ins TEMP-Verzeichnis", e.getMessage(), e);
        } catch (final SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return report;
    }

    public List<File> createReport(final Integer reparaturId, final Integer filialeId) {
        final List<File> reports = new ArrayList<>();
        final Map<String, Object> params = createReportParamsMap(reparaturId, filialeId);
        try (final Connection connection = getConnection()) {
            JasperPrint jprint = JasperFillManager.fillReport(getReportFilePath(REPORT_REPARATUR).getAbsolutePath(), params, connection);
            File tmpFile = File.createTempFile(DATE_FORMAT_TIMESTAMP.format(new Date()) + SUFFIX_REPARATUR, PREFIX_REPORT);
            JasperExportManager.exportReportToPdfFile(jprint, tmpFile.getAbsolutePath());
            reports.add(tmpFile);

            jprint = JasperFillManager.fillReport(getReportFilePath(REPORT_ABHOLBELEG).getAbsolutePath(), params, connection);
            tmpFile = File.createTempFile(DATE_FORMAT_TIMESTAMP.format(new Date()) + SUFFIX_ABHOLBELEG, PREFIX_REPORT);
            JasperExportManager.exportReportToPdfFile(jprint, tmpFile.getAbsolutePath());
            reports.add(tmpFile);
        } catch (final JRException e) {
            LOGGER.error("Fehler beim Laden der Berichte: {} {}", e.getMessage(), e);
        } catch (final IOException e) {
            LOGGER.error("Fehler beim Schreiben des Berichtes ins TEMP-Verzeichnis", e.getMessage(), e);
        } catch (final SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return reports;
    }

    private Map<String, Object> createReportParamsMap(final Integer reparaturId, final Integer filialeId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("PARAM_REPARATUR_ID", reparaturId);
        params.put("PARAM_FILIALE_ID", filialeId);
        return params;
    }

    private Map<String, Object> createReportParamsMap(final Date datumVon, final Date datumBis, final Integer filialeId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("PARAM_ABHOLDATUM_VON", datumVon);
        params.put("PARAM_ABHOLDATUM_BIS", datumBis);
        params.put("PARAM_FILIALE_ID", filialeId);
        return params;
    }

    private Connection getConnection() throws SQLException {
        final String dbUrl = (String) connectionProps.get("connection.url");
        final String dbUname = (String) connectionProps.get("connection.username");
        final String dbPwd = (String) connectionProps.get("connection.password");
        return DriverManager.getConnection(dbUrl, dbUname, dbPwd);
    }

    private static File getReportFilePath(final String filename) {
        return new File(FilenameUtils.concat(REPORTS_BASE_PATH, filename));
    }
}
