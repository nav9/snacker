#!/bin/sh

echo "1. Run for the first time. Caution: This will erase past data if present"
echo "5. Tell me which snack to serve today"
echo "0. Exit"
read number
case $number in
1)
    java -jar snacker.jar gen
    ;;
5)
    java -jar snacker.jar
    ;;
0)
    echo "Bye :-)"
    ;;
esac
