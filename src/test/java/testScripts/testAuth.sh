#Create User - Failure
T=$(curl -s -o /dev/null -w "%{http_code}\\n" --data "usernam=ryanaaaaabryanaaaaabryanaaaaabryanaaaaabryanaaaaabryanaaaaabryanaaaaa&passwor=ey" http://localhost:8080/users)

if [ "$T1=400" ]; then
    echo "Test 1 Passed."
else
    echo "Test 1 Failed."
fi

#Create User - Successful
T2=$(curl -s -o /dev/null -w "%{http_code}\\n" --data "usernam=ryan&passwor=ey" http://localhost:8080/users)
if [ "$T2"="200" ]; then
    echo "Test 2 Passed."
else
    echo "Test 2 Failed."
fi

#Login Authorization Header - Successful
T3=$(curl -s -o /dev/null -w "%{http_code}\\n" --header "Authorization: Basic YnJ5YW46aGV5" -X POST http://localhost:8080/login)
if [ "$T3"="true" ]; then
    echo "Test 3 Passed."
else
    echo "Test 3 Failed."
fi

#Login Auth Header - Failure
T4=$(curl -s -o /dev/null -w "%{http_code}\\n" --header "Authorization: Basic YnJ5YW46aG" -X POST http://localhost:8080/login)

if [ "$T4"="false" ]; then
    echo "Test 4 Passed."
else
    echo "Test 4 Failed."
fi

#Login Params - Successful
T5=$(curl -s -o /dev/null -w "%{http_code}\\n" --data "usernam=ryan&passwor=ey" http://localhost:8080/login)

if [ "$T5"="false" ]; then
    echo "Test 5 Passed."
else
    echo "Test 5 Failed."
fi

#Login Params - Failure
T6=$(curl -s -o /dev/null -w "%{http_code}\\n" --data "usernam=ryan&passwor=eys" http://localhost:8080/login)

if [ "$T6"="false" ]; then
    echo "Test 6 Passed."
else
    echo "Test 6 Failed."
fi

#Search Songs - Successul

#Get Song Detail - Successful
#curl -s -o /dev/null -w "%{http_code}\\n" --header "Authorization: Basic YnJ5YW46aGV5" -X GET http://localhost:8080/songs/2

#Get Song Detail - Failure

