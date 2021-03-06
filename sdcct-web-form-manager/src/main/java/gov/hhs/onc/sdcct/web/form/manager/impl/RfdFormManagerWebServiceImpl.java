package gov.hhs.onc.sdcct.web.form.manager.impl;

import gov.hhs.onc.sdcct.form.manager.FormManager;
import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.rfd.RetrieveClarificationRequestType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormRequestType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormResponseType;
import gov.hhs.onc.sdcct.rfd.RfdException;
import gov.hhs.onc.sdcct.rfd.RfdFormManagerPortType;
import gov.hhs.onc.sdcct.rfd.impl.AnyXmlContentTypeImpl;
import gov.hhs.onc.sdcct.rfd.impl.FormDataTypeImpl;
import gov.hhs.onc.sdcct.rfd.impl.RetrieveFormResponseTypeImpl;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsXmlNames;
import gov.hhs.onc.sdcct.rfd.ws.impl.AbstractRfdFormWebService;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import gov.hhs.onc.sdcct.sdc.impl.SimpleSdcRetrieveFormPackageTypeImpl;
import gov.hhs.onc.sdcct.sdc.impl.XmlPackageImpl;
import gov.hhs.onc.sdcct.web.form.manager.RfdFormManagerWebService;
import gov.hhs.onc.sdcct.xml.impl.XdmDocumentDestination;
import javax.jws.WebService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

@Component("wsFormManagerRfdImpl")
@WebService(portName = RfdWsXmlNames.FORM_MANAGER_PORT, serviceName = RfdWsXmlNames.FORM_MANAGER_SERVICE, targetNamespace = SdcctUris.IHE_ITI_RFD_URN_VALUE)
public class RfdFormManagerWebServiceImpl extends AbstractRfdFormWebService<FormManager> implements RfdFormManagerPortType, RfdFormManagerWebService {
    @Override
    @SuppressWarnings({ "ValidExternallyBoundObject" })
    public RetrieveFormResponseType retrieveForm(RetrieveFormRequestType reqParams) {
        String reqFormIdentifier = reqParams.getWorkflowData().getFormId();

        try {
            FormDesignType formDesign = this.service.findFormDesign(reqFormIdentifier);

            if (formDesign == null) {
                throw new RfdException(String.format("Form (identifier=%s) is unavailable.", reqFormIdentifier));
            }

            return new RetrieveFormResponseTypeImpl()
                .setForm(new FormDataTypeImpl().setContent(new AnyXmlContentTypeImpl()
                    .addAny(this.xmlCodec.encode(new SimpleSdcRetrieveFormPackageTypeImpl().setContent(new XmlPackageImpl().setFormDesign(formDesign)),
                        new XdmDocumentDestination(this.config).getReceiver(), null).getXdmNode().getDocument().getDocumentElement())))
                .setContentType(MimeTypeUtils.APPLICATION_XML_VALUE).setResponseCode(Integer.toString(HttpStatus.OK.value()));
        } catch (RfdException e) {
            throw e;
        } catch (Exception e) {
            throw new RfdException(String.format("Unable to retrieve form (identifier=%s)", reqFormIdentifier), e);
        }
    }

    @Override
    @SuppressWarnings({ "ValidExternallyBoundObject" })
    public RetrieveFormResponseType retrieveClarification(RetrieveClarificationRequestType reqParams) {
        throw new RfdException("Operation is unsupported.");
    }
}
