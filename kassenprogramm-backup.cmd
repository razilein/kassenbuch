net stop <dienstname>

set "YY=%dt:~2,2%" & set "YYYY=%dt:~0,4%" & set "MM=%dt:~4,2%" & set "DD=%dt:~6,2%"
set "HH=%dt:~8,2%" & set "Min=%dt:~10,2%" & set "Sec=%dt:~12,2%"
set "datestamp=%YYYY%%MM%%DD%" & set "timestamp=%HH%%Min%%Sec%"

"C:\Program Files\7-Zip\7z.exe" a -tzip "<Ablageverzeichnis_Backups>\%datestamp%_%timestamp%_backup.zip" "<Zu_sicherndes_Programmverzeichnis>"

net start <dienstname>
