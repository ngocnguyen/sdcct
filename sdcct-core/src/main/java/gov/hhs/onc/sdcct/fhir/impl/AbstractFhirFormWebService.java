package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.fhir.FhirFormWebService;
import gov.hhs.onc.sdcct.form.FormService;
import gov.hhs.onc.sdcct.form.impl.AbstractFormWebService;
import javax.ws.rs.core.Context;
import org.apache.cxf.jaxrs.ext.MessageContext;

public abstract class AbstractFhirFormWebService<T extends FormService> extends AbstractFormWebService<T> implements FhirFormWebService<T> {
    @Context
    protected MessageContext msgContext;
}
