package com.newland.bomc.aspect;

import com.newland.bomc.utils.DesEncrypt;
import com.newland.bomc.utils.SysUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;

/**
 * @author suzj
 * @date 2018/7/2
 */
@Component
@Aspect
@Slf4j
public class ConnectionAspect {

    //FIXME springboot2.0.2.RELEASE版本默认的数据源并不是org.apache.tomcat.jdbc.pool.DataSource，而是：com.zaxxer.hikari.HikariDataSource
    @Around("execution(* com.zaxxer.hikari.HikariDataSource.getConnection(..))")
    public Object getConnectionAround(ProceedingJoinPoint joinPoint) {
        Connection connection = null;
        try {
            connection = (Connection) joinPoint.proceed();
            String currentDB = connection.getCatalog();
            log.info("currentDBName：" + currentDB);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String identify = null;
            String identity = null;
            if (attr != null) {
                HttpServletRequest req = attr.getRequest();
                identify = req.getParameter("identify");
                if (StringUtils.isNotEmpty(req.getHeader("identity"))) {
                    identity = DesEncrypt.decrypt(req.getHeader("identity"));
                }
            }
            if (StringUtils.isNotBlank(identify)) {
                log.info("use identify：" + identify);
                connection.setCatalog(identify);
            } else if (StringUtils.isNotBlank(identity) && !StringUtils.equals(identity, currentDB)) {
                log.info("use DBName：" + SysUtil.getIdentity());
                connection.setCatalog(SysUtil.getIdentity());
            }
        } catch (Throwable throwable) {
            log.info("==> ConnectionAspect error：");
            throwable.printStackTrace();
        }
        return connection;
    }
}
