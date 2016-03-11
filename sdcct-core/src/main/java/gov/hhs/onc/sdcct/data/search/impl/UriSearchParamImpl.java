package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.ResourceEntity;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.AbstractResourceEntity;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import gov.hhs.onc.sdcct.data.search.UriSearchParam;
import java.net.URI;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@DiscriminatorValue(DbTableNames.SEARCH_PARAM_URI)
@Entity(name = "searchParamUri")
@Table(name = DbTableNames.SEARCH_PARAM_URI)
public class UriSearchParamImpl extends AbstractSearchParam<URI> implements UriSearchParam {
    public UriSearchParamImpl(@Nullable ResourceEntity resource, String name, URI value) {
        super(SearchParamType.URI, resource, name, value);
    }

    public UriSearchParamImpl() {
        super(SearchParamType.URI);
    }

    @JoinColumn(name = DbColumnNames.RESOURCE_ENTITY_ID, updatable = false)
    @ManyToOne(optional = false, targetEntity = AbstractResourceEntity.class)
    @Override
    public ResourceEntity getResource() {
        return super.getResource();
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Override
    public URI getValue() {
        return super.getValue();
    }
}
