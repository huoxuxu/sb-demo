HttpClient及其连接池配置
整个线程池中最大连接数 MAX_CONNECTION_TOTAL = 800
路由到某台主机最大并发数，是MAX_CONNECTION_TOTAL（整个线程池中最大连接数）的一个细分 ROUTE_MAX_COUNT = 500
重试次数，防止失败情况 RETRY_COUNT = 3
客户端和服务器建立连接的超时时间 CONNECTION_TIME_OUT = 5000
客户端从服务器读取数据的超时时间 READ_TIME_OUT = 7000
从连接池中获取连接的超时时间 CONNECTION_REQUEST_TIME_OUT = 5000
连接空闲超时，清楚闲置的连接 CONNECTION_IDLE_TIME_OUT = 5000
连接保持存活时间 DEFAULT_KEEP_ALIVE_TIME_MILLIS = 20 * 1000

MaxtTotal和DefaultMaxPerRoute的区别
MaxtTotal是整个池子的大小；
DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分；
比如：MaxtTotal=400，DefaultMaxPerRoute=200，而我只连接到http://hjzgg.com时，到这个主机的并发最多只有200；而不是400；而我连接到http://qyxjj.com 和 http://httls.com时，到每个主机的并发最多只有200；即加起来是400（但不能超过400）。所以起作用的设置是DefaultMaxPerRoute。

HTTP/1.0通过在Header中添加Connection:Keep-Alive来表示支持长连接。

HTTP/1.1默认支持长连接, 除非在Header中显式指定Connection:Close, 才被视为短连接模式。



