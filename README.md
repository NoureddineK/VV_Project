# VV_Project
# Assertion generation

The goal of this assignment is to create a tool that proposes new assertions to strengthen existing test cases. The tool should take as input the path of an existing Maven project, a target test method and the number of assertions to add. The output should be the code of the test case with the new assertions included. To generate the assertions the tool should execute the test case and observe the value of public field and public getter methods. Then, the assertions can verify is these members produce the same values. 
