@echo off
REM ============================================================
REM Cook-List: Regeneracion de la base de datos desde cero
REM - Lee la configuracion de .env (DB_HOST, DB_PORT, DB_NAME,
REM   DB_USERNAME, DB_PASSWORD)
REM - Borra y vuelve a crear la BD vacia
REM - El esquema y los datos iniciales los aplica Flyway al
REM   arrancar la aplicacion (migraciones V1-V4)
REM Uso: regenerar-bd.bat   (requiere psql en el PATH)
REM ============================================================
setlocal

if not exist ".env" (
    echo [ERROR] No se encuentra el archivo .env en la raiz del proyecto.
    exit /b 1
)

REM --- Cargar variables de .env (ignora lineas de comentario #) ---
for /f "usebackq eol=# tokens=1,* delims==" %%a in (".env") do (
    if "%%a"=="DB_HOST" set "DB_HOST=%%b"
    if "%%a"=="DB_PORT" set "DB_PORT=%%b"
    if "%%a"=="DB_NAME" set "DB_NAME=%%b"
    if "%%a"=="DB_USERNAME" set "DB_USERNAME=%%b"
    if "%%a"=="DB_PASSWORD" set "DB_PASSWORD=%%b"
)

set "PGPASSWORD=%DB_PASSWORD%"

REM --- Localizar psql: primero en el PATH y, si no, en la instalacion tipica ---
set "PSQL="
where psql >nul 2>nul && set "PSQL=psql"
if not defined PSQL (
    for /d %%d in ("C:\Program Files\PostgreSQL\*") do (
        if exist "%%d\bin\psql.exe" set "PSQL=%%d\bin\psql.exe"
    )
)
if not defined PSQL (
    echo [ERROR] No se encuentra psql. Anade la carpeta bin de PostgreSQL al PATH.
    exit /b 1
)

echo.
echo [1/3] Conectando a PostgreSQL en %DB_HOST%:%DB_PORT% ...

echo [2/3] Borrando la base de datos "%DB_NAME%" ...
"%PSQL%" -h %DB_HOST% -p %DB_PORT% -U %DB_USERNAME% -d postgres -v ON_ERROR_STOP=1 ^
    -c "DROP DATABASE IF EXISTS %DB_NAME% WITH (FORCE);" ^
    -c "CREATE DATABASE %DB_NAME%;"

if errorlevel 1 (
    echo.
    echo [ERROR] No se pudo regenerar la base de datos.
    echo         Comprueba que PostgreSQL esta arrancado y que psql esta en el PATH.
    exit /b 1
)

echo [3/3] Base de datos "%DB_NAME%" creada vacia.
echo.
echo Listo. Al arrancar la aplicacion Flyway aplicara las migraciones
echo V1-V4: esquema, roles, usuarios iniciales y recetas de prueba.

endlocal
