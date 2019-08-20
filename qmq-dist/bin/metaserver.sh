#!/usr/bin/env bash
set -euo pipefail

QMQ_BIN="${BASH_SOURCE-$0}"
#获取文件夹名称（去掉了最近一层的文件夹路径）
QMQ_BIN="$(dirname "$QMQ_BIN")"
#打印出文件路径
QMQ_BIN_DIR="$(cd "$QMQ_BIN"; pwd)"
#获取启动类完全路径
QMQ_META_MAIN="qunar.tc.qmq.meta.startup.Bootstrap"

#重新加载文件到内存（这里主要是加载两个文件中的环境变量）
. "$QMQ_BIN_DIR/base.sh"
. "$QMQ_BIN_DIR/metaserver-env.sh"

#获取java的路径
if [[ "$JAVA_HOME" != "" ]]; then
  JAVA="$JAVA_HOME/bin/java"
else
  JAVA=java
fi

#设置JAVA_OPTS的启动参数
JAVA_OPTS="$JAVA_OPTS -DQMQ_LOG_DIR=$QMQ_LOG_DIR -Xloggc:${QMQ_LOG_DIR}/metaserver-gc-${TIMESTAMP}.log -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${QMQ_LOG_DIR}"
QMQ_PID_FILE="$QMQ_PID_DIR/metaserver.pid"
QMQ_DAEMON_OUT="$QMQ_LOG_DIR/metaserver.out"

#根据不同的参数，进行条件判断
CMD=${1:-}
case ${CMD} in
#开始
start)
    echo  -n "Starting qmq meta server ... "
    if [[ -f "$QMQ_PID_FILE" ]]; then
      if kill -0 `cat "$QMQ_PID_FILE"` > /dev/null 2>&1; then
         echo already running as process `cat "$QMQ_PID_FILE"`.
         exit 0
      fi
    fi
    nohup "$JAVA" -cp "$CLASSPATH" ${JAVA_OPTS} ${QMQ_META_MAIN} > "$QMQ_DAEMON_OUT" 2>&1 < /dev/null &
    if [[ $? -eq 0 ]]
    then
      /bin/echo -n $! > "$QMQ_PID_FILE"
      if [[ $? -eq 0 ]];
      then
        sleep 1
        echo STARTED
      else
        echo FAILED TO WRITE PID
        exit 1
      fi
    else
      echo SERVER DID NOT START
      exit 1
    fi
    ;;
start-foreground)
    ZOO_CMD=(exec "$JAVA")
    "${ZOO_CMD[@]}" -cp "$CLASSPATH" ${JAVA_OPTS} ${QMQ_META_MAIN}
    ;;
#停止
stop)
    echo -n "Stopping qmq meta server ... "
    if [[ ! -f "$QMQ_PID_FILE" ]]
    then
      echo "no meta server to stop (could not find file $QMQ_PID_FILE)"
    else
      kill -9 $(cat "$QMQ_PID_FILE")
      rm "$QMQ_PID_FILE"
      echo STOPPED
    fi
    exit 0
    ;;
#其他
*)
    echo "Usage: $0 {start|start-foreground|stop}" >&2
esac