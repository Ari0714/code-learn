#!/bin/bash

for host in sr01 sr02 sr03 sr04
do
        echo =============== $host ===============
        ssh $host -p22222 $1
done