#!/bin/bash

ROBOT_PID_FILE="robot_pid_file"

if [ -f ${ROBOT_PID_FILE} ]; then
pid="`cat ${ROBOT_PID_FILE}`"
kill ${pid}
rm -f ${ROBOT_PID_FILE}
fi


echo "robot shutdown successfully."
