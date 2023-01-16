# Kotest: Lifecycle hook invocations differ between platforms (Js/JVM)

Considering the following lifecycle hooks:
1. PROJECT_CONFIG – `AbstractProjectConfig.{before,after}Project`
2. PROJECT – `ProjectExtension.interceptProject`
3. SPEC – `SpecExtension.intercept`
4. TEST_CASE – `TestCaseExtension.intercept`

On the JVM, the order of invocation is
* Entering PROJECT, PROJECT_CONFIG, SPEC, 
* Entering TEST_CASE, Test execution, Exiting TEST_CASE 
* Exiting SPEC, PROJECT_CONFIG, PROJECT  

On Js, the order of invocation is
* Entering PROJECT, PROJECT_CONFIG, SPEC,
* Exiting SPEC, PROJECT_CONFIG, PROJECT
* Entering TEST_CASE, Test execution, Exiting TEST_CASE

This project contains a test suite expecting the JVM behavior: [Test Results](Results.html)

To reproduce: `./gradlew clean allTests`
