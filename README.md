# netty-project
这是基于java+netty+io多路复用实现的socket聊天室，包含自定义协议，以及通信
# Netty聊天室

## 简介

此项目针对聊天室进行代码服务，包括个人聊天，以及群聊，包括登陆，退出，创建群聊加入群聊，查看群聊成员等。

-----------

## 技术栈:springboot+Netty+IO多路复用

------------

- **springboot**：方便导入多种包

- **netty**：本身封装Socket的多个方法

- **IO多路复用**：这方面我不在这里细说，在阻塞io和非阻塞io以及io多路复用之中，io多路复用是最高效的，如果想要详细了解可以去了解下unix操作系统

  -------------------------------

  ## 协议设计

  在针对于消息的发送的时候便于进行对发送消息类型的解析以及有效的解决半包和黏包，再考虑计算机底层对于字节码的跳跃计算，我参照了HTTP协议进行设计。具体如下

  - 魔数，第一时间用来判断是否为有效数据包 【4字节】
  - 版本号  【1字节】
  - 序列化算法 【1字节】
  - 指令类型 【1字节】
  - 请求序号 【4字节】
  - 无意义【1节字】->补充字节
  - 正文长度 【4字节】
  - 消息正文

--------------------------------

以上协议的，提供了类型，长度，以及解析方式，解决了黏包和半包。

# 例子截图

备注：主要是后端服务，所以例子是由控制台进行输出输入

--------------------------
用户客户端登陆
<br/>
<div align=center><img src="https://github.com/nacey5/netty-project/blob/master/images/Socket_Chat_%E5%AE%A2%E6%88%B7%E7%AB%AF%E7%99%BB%E9%99%86%E6%88%90%E5%8A%9F.png"></div>


登陆成功的解析协议
<br>
<div align=center><img src="https://github.com/nacey5/netty-project/blob/master/images/Socket_Chat_%E7%99%BB%E9%99%86%E6%88%90%E5%8A%9F%E7%9A%84%E5%8D%8F%E8%AE%AE%E8%A7%A3%E6%9E%90%E5%BA%8F%E5%88%97.png"></div>

第二用户接收到的消息
<br/>
<div align=center><img src="https://github.com/nacey5/netty-project/blob/master/images/Socket_Chat_%E7%AC%AC%E4%BA%8C%E4%B8%AA%E7%94%A8%E6%88%B7%E6%8E%A5%E6%94%B6%E5%88%B0%E7%9A%84%E6%B6%88%E6%81%AF.png"></div>

p2p服务器解析协议
<br>
<div align=center><img src="https://github.com/nacey5/netty-project/blob/master/images/Socket_Chat_p2p%E6%B6%88%E6%81%AF%E5%9C%A8%E6%9C%8D%E5%8A%A1%E7%9A%84%E5%8D%8F%E8%AE%AE%E8%A7%A3%E6%9E%90.png"></div>

创建群聊成功
<br>
<div align=center><img src="https://github.com/nacey5/netty-project/blob/master/images/Socket_Chat_%E7%BE%A4%E8%81%8A%E5%88%9B%E5%BB%BA%E8%80%85%E5%88%9B%E5%BB%BA%E7%BE%A4%E8%81%8A%E6%88%90%E5%8A%9F.png"></div>

其他用户接收到被拉入群聊的消息
<br>
<div align=center><img src="https://github.com/nacey5/netty-project/blob/master/images/Socket_Chat_%E5%85%B6%E4%BB%96%E7%94%A8%E6%88%B7%E6%8E%A5%E6%94%B6%E5%88%B0%E8%A2%AB%E6%8B%89%E5%85%A5%E7%9A%84%E6%B6%88%E6%81%AF.png"></div>

服务端接收到群聊创建的解析协议
<br>
<div align=center><img src="https://github.com/nacey5/netty-project/blob/master/images/Socket_Chat_%E6%9C%8D%E5%8A%A1%E5%99%A8%E6%8E%A5%E6%94%B6%E5%88%B0%E7%9A%84%E7%BE%A4%E8%81%8A%E5%88%9B%E5%BB%BA%E6%88%90%E5%8A%9F%E7%9A%84%E5%8D%8F%E8%AE%AE%E8%A7%A3%E6%9E%90.png"></div>

非群聊用户加入群聊消息
<br>
<div align=center><img src="https://github.com/nacey5/netty-project/blob/master/images/Socket_Chat_%E9%9D%9E%E7%BE%A4%E8%81%8A%E7%94%A8%E6%88%B7%E5%8A%A0%E5%85%A5%E7%BE%A4%E8%81%8A%E6%B6%88%E6%81%AF.png"></div>

非群聊用户加入群聊群聊用户接收到的消息
<br>
<div align=center><img src="https://github.com/nacey5/netty-project/blob/master/images/Socket_Chat_%E9%9D%9E%E7%BE%A4%E8%81%8A%E7%94%A8%E6%88%B7%E5%8A%A0%E5%85%A5%E7%BE%A4%E8%81%8A%E7%94%A8%E6%88%B7%E6%8E%A5%E6%94%B6%E7%9A%84%E6%B6%88%E6%81%AF.png"></div>

用户发送群聊消息
<br>
<div align=center><img src="https://github.com/nacey5/netty-project/blob/master/images/Socket_Chat_%E7%94%A8%E6%88%B7%E5%8F%91%E9%80%81%E7%BE%A4%E8%81%8A%E6%B6%88%E6%81%AF.png"></div>

群聊消息发送时的服务器解析协议
<br>
<div align=center><img src="https://github.com/nacey5/netty-project/blob/master/images/Socket_Chat_%E7%BE%A4%E8%81%8A%E6%B6%88%E6%81%AF%E5%8F%91%E9%80%81%E7%9A%84%E6%97%B6%E5%80%99%E6%9C%8D%E5%8A%A1%E5%99%A8%E5%8D%8F%E8%AE%AE%E8%A7%A3%E6%9E%90.png"></div>

心跳检测
<br>
<div align=center><img src="https://github.com/nacey5/netty-project/blob/master/images/Socket_Chat_%E5%BF%83%E8%B7%B3%E6%A3%80%E6%B5%8B.png"></div>
