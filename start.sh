#!/bin/bash

ROBOT_PID_FILE="robot_pid_file"

if [ -f ${ROBOT_PID_FILE} ]; then
echo 'Robot PID is already running...' 1>&2
exit 1
fi

nohup java -jar robot-0.0.1-INNERUSE.jar -Dfile.encoding=UTF-8 > /dev/null 2>&1 &
echo $! > ${ROBOT_PID_FILE}