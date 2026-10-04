@echo off
rem Minimal Gradle Wrapper launcher. Requires gradle\wrapper\gradle-wrapper.jar (not bundled, see README).
rem Replace with the official one via:  gradle wrapper --gradle-version 8.13
setlocal
set APP_HOME=%~dp0
set WRAPPER_JAR=%APP_HOME%gradle\wrapper\gradle-wrapper.jar
if not exist "%WRAPPER_JAR%" (
  echo ERROR: %WRAPPER_JAR% not found. Run: gradle wrapper --gradle-version 8.13 1>&2
  exit /b 1
)
if defined JAVA_HOME (set JAVACMD=%JAVA_HOME%\bin\java.exe) else (set JAVACMD=java.exe)
"%JAVACMD%" -Xmx64m -Xms64m -Dorg.gradle.appname=gradlew -classpath "%WRAPPER_JAR%" org.gradle.wrapper.GradleWrapperMain %*
endlocal
