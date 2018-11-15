#!/bin/sh

# Setup variables

export JAVA_HOME=$JAVA_HOME
export FLD_JSVC=/home/apuser/Whale/bin
EXEC=$FLD_JSVC/jsvc
CLASS_PATH="/home/apuser/Whale/modules/CM/lib/*":"/home/apuser/Whale/modules/CM/YRSICmModule.jar"
CLASS=com.main.ModuleMain
USER=apuser
PID=/home/apuser/Whale/modules/CM/Module.pid
LOG_OUT=/home/apuser/Whale/logs/cm_jsvc.out
LOG_ERR=/home/apuser/Whale/logs/cm_jsvc.err

do_exec()
{
$EXEC -home "$JAVA_HOME" -cp $CLASS_PATH -user $USER -outfile $LOG_OUT -errfile $LOG_ERR -pidfile $PID $1 $CLASS
}

case "$1" in
start)
do_exec
;;
stop)
do_exec "-stop"
;;
restart)
if [ -f "$PID" ]; then
do_exec "-stop"
do_exec
else
echo "service not running, will do nothing"
exit 1
fi
;;
*)
echo "usage: CM Module {start|stop|restart}" >&2
exit 3
;;
esac
