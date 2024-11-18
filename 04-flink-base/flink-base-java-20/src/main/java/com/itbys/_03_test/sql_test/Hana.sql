CREATE TABLE source
(
    uuid        string,
    name      string,
    age     string,
    ts   string
) WITH (
      'connector' = 'saphana-x',
      'url' = 'jdbc:sap://localhost:39015',
      'table-name' = 'T_CHARACTER',
      'username' = 'SYSTEM',
      'password' = 'Abc!@#579'
      ,'scan.parallelism' = '1' -- 并行度大于1时，必须指定scan.partition.column。默认：1
      ,'scan.fetch-size' = '2' -- 每次从数据库中fetch大小。默认：1024条
      ,'scan.query-timeout' = '10' -- 数据库连接超时时间。默认：1秒
      ,'scan.partition.column' = 'id' -- 多并行度读取的切分字段，多并行度下必需要设置。无默认
      ,'scan.partition.strategy' = 'range' -- 数据分片策略。默认：range
      -- ,'scan.increment.column' = 'id' -- 增量字段名称，如果配置了该字段，目前并行度只能为1。非必填，无默认
      -- ,'scan.increment.column-type' = 'int' -- 增量字段类型。非必填，无默认
      ,'scan.start-location' = '109' -- 增量字段开始位置。非必填，无默认
      );

           "  uuid varchar(20),\n" +
                "  name varchar(10),\n" +
                "  age int,\n" +
                "  ts timestamp(3)\n" +


CREATE TABLE sourceTable (
                 uuid varchar(20),
                  name varchar(10),
                  age varchar(10),
                  ts varchar(20)
                ) WITH (
                  'connector' = 'datagen',
                  'rows-per-second' = '1'
                );


CREATE TABLE sinkTable
(
    uuid      string,
    name      string,
    age     string,
    ts   string
) WITH (
      'connector' = 'saphana-x',
      'url' = 'jdbc:sap://10.118.1.208:30015',
      'table-name' = 'SOURCETABLE',
      'username' = 'u008bigdata',
      'password' = 'A9AOQGoQq3',
      'sink.buffer-flush.max-rows' = '1024', -- 批量写数据条数，默认：1024
      'sink.buffer-flush.interval' = '10000', -- 批量写时间间隔，默认：10000毫秒
      'sink.all-replace' = 'true', -- 解释如下(其他rdb数据库类似)：默认：false
      'sink.parallelism' = '1'    -- 写入结果的并行度，默认：null
      );


insert into sinkTable
select *
from sourceTable;