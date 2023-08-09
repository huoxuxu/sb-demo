xls：97-03格式
xlsx：07格式
=================================================================
HSSF－ 提供读写Microsoft Excel XLS格式档案的功能。
XSSF－ 提供读写Microsoft Excel OOXML XLSX格式档案的功能。
HWPF－ 提供读写Word(97-2003) 的 Java 组件，XWPF是 POI 支持 Word 2007+ 的 Java组件，提供简单文件的读写功能；
HSLF－ 提供读写Microsoft PowerPoint格式档案的功能。
HDGF － 提供读Microsoft Visio格式档案的功能。
HPBF － 提供读Microsoft Publisher格式档案的功能。
HSMF－ 提供读Microsoft Outlook格式档案的功能。

HSSF文件和XSSF文件两种操作方式是一样的，但是二者又有很大的区别：HSSF写的速度很快，但是最多只能写65536条数据；
XSSF能够写大量的数据，但是写的速度很慢，其次XSSF的写操作是直接在内存中写数据，写完后再复制到磁盘，这样就会造成内存溢出的问题，
因此为了解决内存溢出问题，poi有为我们提供了一个SXSSF对象进行.xlsx文件的操作