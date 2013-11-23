@echo off
title Login Server Console
:start
set user=root
set pass=root
set DBname=1
set DBHost=localhost
set ctime=%TIME:~0,2%
if "%ctime:~0,1%" == " " (
set ctime=0%ctime:~1,1%
)
set ctime=%ctime%'%TIME:~3,2%'%TIME:~6,2%
echo.
echo Making a full backup into %DATE%-%ctime%_backup_full.sql
echo.
mysqldump.exe --add-drop-table -h localhost -u root --password=gYT687HJGH5456GHFG l2wtl > backup/%DATE%-%ctime%_Backup_full_LS.sql
echo.
echo Backup complite %DATE%-%ctime%_backup_full.sql
echo.
echo %DATE% %TIME% Game server is running !!! > gameserver_is_running.tmp
echo Starting LoginServer.
echo.
java -server -Dfile.encoding=UTF-8 -Xms64m -Xmx64m -cp config;../serverslibs/*; l2p.loginserver.LoginServer
if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end
:restart
echo.
echo Server restarted ...
echo.
goto start
:error
echo.
echo Server terminated abnormaly ...
echo.
:end
echo.
echo Server terminated ...
echo.

pause
