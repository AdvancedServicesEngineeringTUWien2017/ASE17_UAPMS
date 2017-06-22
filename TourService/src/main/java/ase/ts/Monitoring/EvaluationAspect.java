package ase.ts.Monitoring;

import ase.monitoring.Models.SharedMonitoringEntry;
import ase.monitoring.Service.MonitoringForwardingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.UUID;

/**
 * Created by Tommi on 16.06.2017.
 */
@Aspect
@Component
public class EvaluationAspect {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MonitoringForwardingService service;

    @Around("execution(public * ase.ts.RestInterface.TourServiceRestController.*(..))")
    public Object restInterfaceTracer(ProceedingJoinPoint joinPoint) throws Throwable{
        Object result = null;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Throwable exception = null;
        try {
            result = joinPoint.proceed();
        }
        catch (Throwable e){
            exception = e;
        }
        stopWatch.stop();

        try {
            SharedMonitoringEntry entry = new SharedMonitoringEntry();
            entry.setGuid(UUID.randomUUID());
            entry.setResponseTime(stopWatch.getTotalTimeSeconds());
            String operation = joinPoint.getSignature().getName();
            entry.setOperation(operation);
            service.forward(entry);
            Object[] args = joinPoint.getArgs();
            if (args != null) {
                String parameter = mapper.writeValueAsString(args);
                entry.setRequest(parameter);
            }
            if(result != null){
                String res = mapper.writeValueAsString(result);
                entry.setResponse(res);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(exception != null){
            throw exception;
        } else{
            return result;
        }
    }
}
