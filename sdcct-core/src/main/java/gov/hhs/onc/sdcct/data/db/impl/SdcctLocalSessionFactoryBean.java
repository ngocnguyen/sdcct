package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.cache.impl.CacheRegionFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.boot.registry.internal.BootstrapServiceRegistryImpl;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.boot.spi.SessionFactoryBuilderFactory;
import org.hibernate.boot.spi.SessionFactoryBuilderImplementor;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.search.engine.impl.DefaultMutableEntityIndexBinding;
import org.hibernate.search.engine.impl.MutableEntityIndexBinding;
import org.hibernate.search.engine.integration.impl.ExtendedSearchIntegrator;
import org.hibernate.search.hcore.util.impl.ContextHelper;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;

public class SdcctLocalSessionFactoryBean extends LocalSessionFactoryBean implements BeanClassLoaderAware {
    private class SdcctSessionFactoryBuilderFactory implements SessionFactoryBuilderFactory {
        @Override
        public SessionFactoryBuilderImplementor getSessionFactoryBuilder(MetadataImplementor metadata, SessionFactoryBuilderImplementor delegate) {
            SdcctLocalSessionFactoryBean.this.metadata = metadata;

            delegate.applyStatementInspector(SdcctLocalSessionFactoryBean.this.statementInspector);

            return delegate;
        }
    }

    private class SdcctClassLoaderService extends ClassLoaderServiceImpl {
        private final static long serialVersionUID = 0L;

        private SdcctSessionFactoryBuilderFactory sessionFactoryBuilderFactory;

        public SdcctClassLoaderService(SdcctSessionFactoryBuilderFactory sessionFactoryBuilderFactory) {
            super(SdcctLocalSessionFactoryBean.this.beanClassLoader);

            this.sessionFactoryBuilderFactory = sessionFactoryBuilderFactory;
        }

        @Override
        public <S> Collection<S> loadJavaServices(Class<S> serviceClass) {
            return (serviceClass.equals(SessionFactoryBuilderFactory.class)
                ? Collections.singleton(serviceClass.cast(this.sessionFactoryBuilderFactory)) : super.loadJavaServices(serviceClass));
        }
    }

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiredMembersInspection" })
    private LoggingStatementInspector statementInspector;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiredMembersInspection" })
    private LoggingEntityInterceptor entityInterceptor;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiredMembersInspection" })
    private LoggingEntityIndexingInterceptor entityIndexingInterceptor;

    private AttributeConverter<?, ?>[] attrConvs;
    private ClassLoader beanClassLoader;
    private CacheRegionFactory cacheRegionFactory;
    private MetadataImplementor metadata;

    @Override
    protected SessionFactory buildSessionFactory(LocalSessionFactoryBuilder sessionFactoryBuilder) {
        if (this.hasAttributeConverters()) {
            Stream.of(this.attrConvs).forEach(sessionFactoryBuilder::addAttributeConverter);
        }

        sessionFactoryBuilder.setInterceptor(this.entityInterceptor);

        StandardServiceRegistryBuilder serviceRegistryBuilder = sessionFactoryBuilder.getStandardServiceRegistryBuilder();

        ((BootstrapServiceRegistryImpl) serviceRegistryBuilder.getBootstrapServiceRegistry()).locateServiceBinding(ClassLoaderService.class).setService(
            new SdcctClassLoaderService(new SdcctSessionFactoryBuilderFactory()));

        serviceRegistryBuilder.addService(RegionFactory.class, this.cacheRegionFactory);

        MetadataService metadataService = new MetadataService();
        serviceRegistryBuilder.addService(MetadataService.class, metadataService);

        SessionFactoryImplementor sessionFactory = ((SessionFactoryImplementor) super.buildSessionFactory(sessionFactoryBuilder));
        ExtendedSearchIntegrator searchIntegrator = ContextHelper.getSearchintegratorBySFI(sessionFactory);

        searchIntegrator.getIndexBindings().replaceAll(
            (entityClass, entityIndexBinding) -> {
                MutableEntityIndexBinding newEntityIndexBinding =
                    new DefaultMutableEntityIndexBinding(entityIndexBinding.getSelectionStrategy(), entityIndexBinding.getSimilarity(), entityIndexBinding
                        .getIndexManagers(), this.entityIndexingInterceptor);
                newEntityIndexBinding.setDocumentBuilderIndexedEntity(entityIndexBinding.getDocumentBuilder());

                return newEntityIndexBinding;
            });

        metadataService.initialize(this.metadata, searchIntegrator);

        return sessionFactory;
    }

    public boolean hasAttributeConverters() {
        return !ArrayUtils.isEmpty(this.attrConvs);
    }

    public AttributeConverter<?, ?>[] getAttributeConverters() {
        return this.attrConvs;
    }

    public void setAttributeConverters(AttributeConverter<?, ?> ... attrConvs) {
        this.attrConvs = attrConvs;
    }

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    public CacheRegionFactory getCacheRegionFactory() {
        return this.cacheRegionFactory;
    }

    public void setCacheRegionFactory(CacheRegionFactory cacheRegionFactory) {
        this.cacheRegionFactory = cacheRegionFactory;
    }
}