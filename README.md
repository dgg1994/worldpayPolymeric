## onetoken u卡服务端
### aws自动部署
### 注意当前分支的数据库连接地址

   /src/main/resources/application.properties中的数据库配置，不能配置错 ，main分支配置生产数据库，dev分支配置测试数据库

### 开发使用其他分支，需要发版就合并到main或是dev分支

#### dev开发域名：http://card.onetoken.info/


    测试：http://card.onetoken.info/onetoken/sysNavigateConfig/find


    dev 数据库配置（aws RDB ， 端口3306，允许任意ip登录）：
    
    ```
        url: jdbc:mysql://onetoken-ucard-dev.cnwoica2yo1h.ap-east-1.rds.amazonaws.com:3306/onetokencard?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8
        username: admin
        password: ymWwiCRhnaHkb8iE
    ```

#### main生产域名：http://card.1token.me/

    main 数据库配置（aws RDB ， 端口3306，必须指定ip登录）：
    
    ```
        url: jdbc:mysql://onetoken-ucard-pro.cnwoica2yo1h.ap-east-1.rds.amazonaws.com:3306/onetokencard?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8
        username: admin
        password: 
    ```

