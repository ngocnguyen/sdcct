package gov.hhs.onc.sdcct.utils;

import com.github.sebhoss.warnings.CompilerWarnings;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.builder.Builder;
import org.springframework.aop.Advisor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.target.SingletonTargetSource;

public final class SdcctAopUtils {
    public static class SdcctSingletonTargetSource extends SingletonTargetSource {
        private final static long serialVersionUID = 0L;

        private Class<?> targetClass;

        public SdcctSingletonTargetSource(Object target, Class<?> targetClass) {
            super(target);

            this.targetClass = targetClass;
        }

        @Override
        public Class<?> getTargetClass() {
            return this.targetClass;
        }
    }

    @FunctionalInterface
    public static interface SdcctMethodInterceptor<T> extends MethodInterceptor {
        @Nullable
        @Override
        @SuppressWarnings({ CompilerWarnings.UNCHECKED })
        public default Object invoke(MethodInvocation invocation) throws Throwable {
            Method method = invocation.getMethod();

            return this.invoke(invocation, method, method.getName(), invocation.getArguments(), ((T) invocation.getThis()));
        }

        @Nullable
        public Object invoke(MethodInvocation invocation, Method method, String methodName, Object[] args, @Nullable T target) throws Throwable;
    }

    public static class SdcctProxyBuilder<T> implements Builder<T> {
        private AspectJProxyFactory factory = new AspectJProxyFactory();

        public SdcctProxyBuilder(Class<T> targetClass, Object target) {
            this.factory.setTargetSource(new SdcctSingletonTargetSource(target, targetClass));

            if (!targetClass.isInterface()) {
                this.factory.setProxyTargetClass(true);
            }
        }

        @Override
        public T build() {
            return this.factory.getProxy();
        }

        public <U> SdcctProxyBuilder<T> addMethodAdvice(MethodMatcher methodMatcher, SdcctMethodInterceptor<U> advice) {
            return this.addMethodAdvice(null, methodMatcher, advice);
        }

        public <U> SdcctProxyBuilder<T> addMethodAdvice(@Nullable ClassFilter classFilter, @Nullable MethodMatcher methodMatcher,
            SdcctMethodInterceptor<U> advice) {
            return this.addMethodAdvice(new ComposablePointcut(((classFilter != null) ? classFilter : ClassFilter.TRUE), ((methodMatcher != null)
                ? methodMatcher : MethodMatcher.TRUE)), advice);
        }

        public <U> SdcctProxyBuilder<T> addMethodAdvice(Pointcut pointcut, SdcctMethodInterceptor<U> advice) {
            return this.addAdvisors(new DefaultPointcutAdvisor(pointcut, advice));
        }

        public SdcctProxyBuilder<T> addAdvisors(Advisor ... advisors) {
            this.factory.addAdvisors(advisors);

            return this;
        }

        public AspectJProxyFactory getFactory() {
            return this.factory;
        }
    }

    private SdcctAopUtils() {
    }
}
