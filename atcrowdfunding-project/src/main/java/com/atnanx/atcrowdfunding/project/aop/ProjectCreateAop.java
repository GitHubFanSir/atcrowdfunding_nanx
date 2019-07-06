package com.atnanx.atcrowdfunding.project.aop;

import com.atnanx.atcrowdfunding.project.common.annotation.TempProjectCreate;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
@Slf4j
//@Component
public class ProjectCreateAop {


    @Pointcut(value="@annotation(com.atnanx.atcrowdfunding.project.common.annotation.TempProjectCreate)")
    public void cutCreateProject(){

    }

    @Before("cutCreateProject()")
    public void doCheckProjectExist(ProceedingJoinPoint point){
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();
        TempProjectCreate tempProjectCreate = method.getAnnotation(TempProjectCreate.class);
        Object[] tempProjectCreates = tempProjectCreate.value();

      /*  String tempProjectToken = vo.getProjectToken();
        String tempProjectRedisKey =  Const.projectInfo.REDIS_TEMP_PROJECT_PREFIX+tempProjectToken;

        String tempProject = stringRedisTemplate.opsForValue().get(tempProjectRedisKey);

        ProjectRedisStorageVo projectRedisStorageVo = JsonUtil.Json2Obj(tempProject, ProjectRedisStorageVo.class);
        if (projectRedisStorageVo==null){
            //在这里判断是为了避免redis数据有误，转化不成对象
            return ServerResponse.createByErrorMessage("临时创建的项目失效或数据出错，请重新创建");
        }*/
    }

}
