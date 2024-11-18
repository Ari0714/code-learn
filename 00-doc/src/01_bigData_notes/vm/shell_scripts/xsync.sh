#!/bin/bash

#1. 判断参数个数
if [ $# -lt 1 ]
then
        echo "请输入参数"
        exit;
fi

if [ $# -eq 1 ]
then

#2. 遍历集群所有机器
for host in sr01 sr02 sr03 sr04
do
  echo =================$host=========================
    #3. 遍历所有目录，挨个发送
      for file in $@
      do
        #4. 判断文件是否存在
        if [ -e $file ]
          then
           #5. 获取父目录
           pdir=$(cd -P $(dirname $file); pwd)
           #6. 获取文件名
           fname=$(basename $file)
           ssh $host "mkdir -p $pdir"
           rsync -e 'ssh -p 22222' -av $pdir/$fname $host:$pdir
          else
            echo "文件不存在！"
          fi
 done
done
fi