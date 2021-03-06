package gov.hhs.onc.sdcct;

import gov.hhs.onc.sdcct.utils.SdcctClassUtils;
import org.apache.commons.lang3.ClassUtils;

public final class SdcctPackages {
    public final static String ROOT_NAME = "gov.hhs.onc.sdcct";
    public final static Package ROOT = Package.getPackage(ROOT_NAME);

    public final static String BEANS_NAME = ROOT_NAME + ".beans";
    public final static Package BEANS = Package.getPackage(BEANS_NAME);
    public final static String BEANS_IMPL_NAME = BEANS_NAME + ClassUtils.PACKAGE_SEPARATOR + SdcctClassUtils.IMPL_PKG_NAME;
    public final static Package BEANS_IMPL = Package.getPackage(BEANS_IMPL_NAME);

    public final static String FHIR_NAME = ROOT_NAME + ".fhir";
    public final static Package FHIR = Package.getPackage(FHIR_NAME);
    public final static String FHIR_IMPL_NAME = FHIR_NAME + ClassUtils.PACKAGE_SEPARATOR + SdcctClassUtils.IMPL_PKG_NAME;
    public final static Package FHIR_IMPL = Package.getPackage(FHIR_IMPL_NAME);

    public final static String FHIR_XHTML_NAME = FHIR_NAME + ".xhtml";
    public final static Package FHIR_XHTML = Package.getPackage(FHIR_XHTML_NAME);
    public final static String FHIR_XHTML_IMPL_NAME = FHIR_XHTML_NAME + ClassUtils.PACKAGE_SEPARATOR + SdcctClassUtils.IMPL_PKG_NAME;
    public final static Package FHIR_XHTML_IMPL = Package.getPackage(FHIR_XHTML_IMPL_NAME);

    public final static String RFD_NAME = ROOT_NAME + ".rfd";
    public final static Package RFD = Package.getPackage(RFD_NAME);
    public final static String RFD_IMPL_NAME = RFD_NAME + ClassUtils.PACKAGE_SEPARATOR + SdcctClassUtils.IMPL_PKG_NAME;
    public final static Package RFD_IMPL = Package.getPackage(RFD_IMPL_NAME);

    public final static String SCHEMATRON_NAME = ROOT_NAME + ".schematron";
    public final static Package SCHEMATRON = Package.getPackage(SCHEMATRON_NAME);
    public final static String SCHEMATRON_IMPL_NAME = SCHEMATRON_NAME + ClassUtils.PACKAGE_SEPARATOR + SdcctClassUtils.IMPL_PKG_NAME;
    public final static Package SCHEMATRON_IMPL = Package.getPackage(SCHEMATRON_IMPL_NAME);

    public final static String SCHEMATRON_SVRL_NAME = SCHEMATRON_NAME + ".svrl";
    public final static Package SCHEMATRON_SVRL = Package.getPackage(SCHEMATRON_SVRL_NAME);
    public final static String SCHEMATRON_SVRL_IMPL_NAME = SCHEMATRON_SVRL_NAME + ClassUtils.PACKAGE_SEPARATOR + SdcctClassUtils.IMPL_PKG_NAME;
    public final static Package SCHEMATRON_SVRL_IMPL = Package.getPackage(SCHEMATRON_SVRL_IMPL_NAME);

    public final static String SDC_NAME = ROOT_NAME + ".sdc";
    public final static Package SDC = Package.getPackage(SDC_NAME);
    public final static String SDC_IMPL_NAME = SDC_NAME + ClassUtils.PACKAGE_SEPARATOR + SdcctClassUtils.IMPL_PKG_NAME;
    public final static Package SDC_IMPL = Package.getPackage(SDC_IMPL_NAME);

    private SdcctPackages() {
    }
}
