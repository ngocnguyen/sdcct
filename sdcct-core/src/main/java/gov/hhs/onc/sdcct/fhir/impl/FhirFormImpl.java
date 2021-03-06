package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.fhir.FhirForm;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.form.impl.AbstractSdcctForm;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;

public class FhirFormImpl extends AbstractSdcctForm<Questionnaire> implements FhirForm {
    public FhirFormImpl(String name, ResourceSource src) {
        super(SpecificationType.FHIR, Questionnaire.class, QuestionnaireImpl.class, name, src);
    }
}
