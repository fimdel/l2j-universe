@echo off

set PATH=%PATH%;C:\Program Files (x86)\VertrigoServ\Mysql\bin

set USER=root
set PASS=vertrigo
set DBNAME=lv_test
set DBHOST=127.0.0.1

mysql -h %DBHOST% -u %USER% --password=%PASS% -Bse "use %DBNAME%" > nul 2>&1

if errorlevel 9009 goto notfound
if errorlevel 1 goto error
goto end

:error
echo Can't use %DBNAME%!
exit /b 1

:notfound
echo Can't find mysql binary!
exit /b 1

:END