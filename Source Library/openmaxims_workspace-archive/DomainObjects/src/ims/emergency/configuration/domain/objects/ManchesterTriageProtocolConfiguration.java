//#############################################################################
//#                                                                           #
//#  Copyright (C) <2014>  <IMS MAXIMS>                                       #
//#                                                                           #
//#  This program is free software: you can redistribute it and/or modify     #
//#  it under the terms of the GNU Affero General Public License as           #
//#  published by the Free Software Foundation, either version 3 of the       #
//#  License, or (at your option) any later version.                          # 
//#                                                                           #
//#  This program is distributed in the hope that it will be useful,          #
//#  but WITHOUT ANY WARRANTY; without even the implied warranty of           #
//#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the            #
//#  GNU Affero General Public License for more details.                      #
//#                                                                           #
//#  You should have received a copy of the GNU Affero General Public License #
//#  along with this program.  If not, see <http://www.gnu.org/licenses/>.    #
//#                                                                           #
//#############################################################################
//#EOH
/*
 * This code was generated
 * Copyright (C) 1995-2004 IMS MAXIMS plc. All rights reserved.
 * IMS Development Environment (version 1.80 build 5007.25751)
 * WARNING: DO NOT MODIFY the content of this file
 * Generated: 16/04/2014, 12:34
 *
 */
package ims.emergency.configuration.domain.objects;

/**
 * 
 * @author Neil McAnaspie
 * Generated.
 */


public class ManchesterTriageProtocolConfiguration extends ims.domain.DomainObject implements ims.domain.SystemInformationRetainer, java.io.Serializable {
	public static final int CLASSID = 1087100010;
	private static final long serialVersionUID = 1087100010L;
	public static final String CLASSVERSION = "${ClassVersion}";

	@Override
	public boolean shouldCapQuery()
	{
		return true;
	}

	/** Problem */
	private ims.clinical.configuration.domain.objects.ClinicalProblem problem;
	/** ProtocolName */
	private String protocolName;
	/** ProtocolDescription */
	private String protocolDescription;
	/** Default Priority */
	private ims.domain.lookups.LookupInstance defaultPriority;
	/** ActiveStatus */
	private ims.domain.lookups.LookupInstance activeStatus;
	/** Discriminators
	  * Collection of ims.emergency.configuration.domain.objects.ProtocolDiscriminator.
	  */
	private java.util.List discriminators;
	/** 
	  * Collection of ims.core.clinical.domain.objects.TaxonomyMap.
	  */
	private java.util.List taxonomyMap;
	/** SystemInformation */
	private ims.domain.SystemInformation systemInformation = new ims.domain.SystemInformation();
    public ManchesterTriageProtocolConfiguration (Integer id, int ver)
    {
    	super(id, ver);
    }
    public ManchesterTriageProtocolConfiguration ()
    {
    	super();
    }
    public ManchesterTriageProtocolConfiguration (Integer id, int ver, Boolean includeRecord)
    {
    	super(id, ver, includeRecord);
    }
	public Class getRealDomainClass()
	{
		return ims.emergency.configuration.domain.objects.ManchesterTriageProtocolConfiguration.class;
	}


	public ims.clinical.configuration.domain.objects.ClinicalProblem getProblem() {
		return problem;
	}
	public void setProblem(ims.clinical.configuration.domain.objects.ClinicalProblem problem) {
		this.problem = problem;
	}

	public String getProtocolName() {
		return protocolName;
	}
	public void setProtocolName(String protocolName) {
		if ( null != protocolName && protocolName.length() > 100 ) {
			throw new ims.domain.exceptions.DomainRuntimeException("MaxLength ($MaxLength) exceeded for protocolName. Tried to set value: "+
				protocolName);
		}
		this.protocolName = protocolName;
	}

	public String getProtocolDescription() {
		return protocolDescription;
	}
	public void setProtocolDescription(String protocolDescription) {
		if ( null != protocolDescription && protocolDescription.length() > 500 ) {
			throw new ims.domain.exceptions.DomainRuntimeException("MaxLength ($MaxLength) exceeded for protocolDescription. Tried to set value: "+
				protocolDescription);
		}
		this.protocolDescription = protocolDescription;
	}

	public ims.domain.lookups.LookupInstance getDefaultPriority() {
		return defaultPriority;
	}
	public void setDefaultPriority(ims.domain.lookups.LookupInstance defaultPriority) {
		this.defaultPriority = defaultPriority;
	}

	public ims.domain.lookups.LookupInstance getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(ims.domain.lookups.LookupInstance activeStatus) {
		this.activeStatus = activeStatus;
	}

	public java.util.List getDiscriminators() {
		if ( null == discriminators ) {
			discriminators = new java.util.ArrayList();
		}
		return discriminators;
	}
	public void setDiscriminators(java.util.List paramValue) {
		this.discriminators = paramValue;
	}

	public java.util.List getTaxonomyMap() {
		if ( null == taxonomyMap ) {
			taxonomyMap = new java.util.ArrayList();
		}
		return taxonomyMap;
	}
	public void setTaxonomyMap(java.util.List paramValue) {
		this.taxonomyMap = paramValue;
	}

	public ims.domain.SystemInformation getSystemInformation() {
		if (systemInformation == null) systemInformation = new ims.domain.SystemInformation();
		return systemInformation;
	}


	/**
	 * isConfigurationObject
	 * Taken from the Usage property of the business object, this method will return
	 * a boolean indicating whether this is a configuration object or not
	 * Configuration = true, Instantiation = false
	 */
	public static boolean isConfigurationObject()
	{
		if ( "Configuration".equals("Configuration") )
			return true;
		else
			return false;
	}





	public int getClassId() {
		return CLASSID;
	}

	public String getClassVersion()
	{
		return CLASSVERSION;
	}

	public String toAuditString()
	{
		StringBuffer auditStr = new StringBuffer();
		
		auditStr.append("\r\n*problem* :");
		if (problem != null)
		{
			auditStr.append(toShortClassName(problem));
				
		    auditStr.append(problem.getId());
		}
	    auditStr.append("; ");
		auditStr.append("\r\n*protocolName* :");
		auditStr.append(protocolName);
	    auditStr.append("; ");
		auditStr.append("\r\n*protocolDescription* :");
		auditStr.append(protocolDescription);
	    auditStr.append("; ");
		auditStr.append("\r\n*defaultPriority* :");
		if (defaultPriority != null)
			auditStr.append(defaultPriority.getText());
	    auditStr.append("; ");
		auditStr.append("\r\n*activeStatus* :");
		if (activeStatus != null)
			auditStr.append(activeStatus.getText());
	    auditStr.append("; ");
		auditStr.append("\r\n*discriminators* :");
		if (discriminators != null)
		{
		int i6=0;
		for (i6=0; i6<discriminators.size(); i6++)
		{
			if (i6 > 0)
				auditStr.append(",");
			ims.emergency.configuration.domain.objects.ProtocolDiscriminator obj = (ims.emergency.configuration.domain.objects.ProtocolDiscriminator)discriminators.get(i6);
		    if (obj != null)
			{
				if (i6 == 0)
				{
				auditStr.append(toShortClassName(obj));
				auditStr.append("[");
				}
		        auditStr.append(obj.getId());
			}
		}
		if (i6 > 0)
			auditStr.append("] " + i6);
		}
	    auditStr.append("; ");
		auditStr.append("\r\n*taxonomyMap* :");
		if (taxonomyMap != null)
		{
		int i7=0;
		for (i7=0; i7<taxonomyMap.size(); i7++)
		{
			if (i7 > 0)
				auditStr.append(",");
			ims.core.clinical.domain.objects.TaxonomyMap obj = (ims.core.clinical.domain.objects.TaxonomyMap)taxonomyMap.get(i7);
		    if (obj != null)
			{
				if (i7 == 0)
				{
				auditStr.append(toShortClassName(obj));
				auditStr.append("[");
				}
		        auditStr.append(obj.toString());
			}
		}
		if (i7 > 0)
			auditStr.append("] " + i7);
		}
	    auditStr.append("; ");
		return auditStr.toString();
	}
	
	public String toXMLString()
	{
		return toXMLString(new java.util.HashMap());
	}
	
	public String toXMLString(java.util.HashMap domMap)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<class type=\"" + this.getClass().getName() + "\" ");		
		sb.append(" id=\"" + this.getId() + "\""); 
		sb.append(" source=\"" + ims.configuration.EnvironmentConfig.getImportExportSourceName() + "\" ");
		sb.append(" classVersion=\"" + this.getClassVersion() + "\" ");
		sb.append(" component=\"" + this.getIsComponentClass() + "\" >");
		
		if (domMap.get(this) == null)
		{
			domMap.put(this, this);
			sb.append(this.fieldsToXMLString(domMap));
		}
		sb.append("</class>");
		
		String keyClassName = "ManchesterTriageProtocolConfiguration";
		String externalSource = ims.configuration.EnvironmentConfig.getImportExportSourceName();
		ims.configuration.ImportedObject impObj = (ims.configuration.ImportedObject)domMap.get(keyClassName + "_" + externalSource + "_" + this.getId());
		if (impObj == null)
		{
    		impObj = new ims.configuration.ImportedObject();
    		impObj.setExternalId(this.getId());
    		impObj.setExternalSource(externalSource);
    		impObj.setDomainObject(this);
			impObj.setLocalId(this.getId()); 
    		impObj.setClassName(keyClassName); 
			domMap.put(keyClassName + "_" + externalSource + "_" + this.getId(), impObj);
		}
		
		return sb.toString();
	}

	public String fieldsToXMLString(java.util.HashMap domMap)
	{
		StringBuffer sb = new StringBuffer();
		if (this.getProblem() != null)
		{
			sb.append("<problem>");
			sb.append(this.getProblem().toXMLString(domMap)); 	
			sb.append("</problem>");		
		}
		if (this.getProtocolName() != null)
		{
			sb.append("<protocolName>");
			sb.append(ims.framework.utils.StringUtils.encodeXML(this.getProtocolName().toString()));
			sb.append("</protocolName>");		
		}
		if (this.getProtocolDescription() != null)
		{
			sb.append("<protocolDescription>");
			sb.append(ims.framework.utils.StringUtils.encodeXML(this.getProtocolDescription().toString()));
			sb.append("</protocolDescription>");		
		}
		if (this.getDefaultPriority() != null)
		{
			sb.append("<defaultPriority>");
			sb.append(this.getDefaultPriority().toXMLString()); 
			sb.append("</defaultPriority>");		
		}
		if (this.getActiveStatus() != null)
		{
			sb.append("<activeStatus>");
			sb.append(this.getActiveStatus().toXMLString()); 
			sb.append("</activeStatus>");		
		}
		if (this.getDiscriminators() != null)
		{
			if (this.getDiscriminators().size() > 0 )
			{
			sb.append("<discriminators>");
			sb.append(ims.domain.DomainObject.toXMLString(this.getDiscriminators(), domMap));
			sb.append("</discriminators>");		
			}
		}
		if (this.getTaxonomyMap() != null)
		{
			if (this.getTaxonomyMap().size() > 0 )
			{
			sb.append("<taxonomyMap>");
			sb.append(ims.domain.DomainObject.toXMLString(this.getTaxonomyMap(), domMap));
			sb.append("</taxonomyMap>");		
			}
		}
		return sb.toString();
	}
		
	public static java.util.List fromListXMLString(org.dom4j.Element el, ims.domain.DomainFactory factory, java.util.List list, java.util.HashMap domMap) throws Exception
	{
		if (list == null)
		 list = new java.util.ArrayList();
		fillListFromXMLString(list, el, factory, domMap);
		return list;
	}
	
	public static java.util.Set fromSetXMLString(org.dom4j.Element el, ims.domain.DomainFactory factory, java.util.Set set, java.util.HashMap domMap) throws Exception
	{
		if (set == null)
			 set = new java.util.HashSet();
		fillSetFromXMLString(set, el, factory, domMap);
		return set;
	}
	
	private static void fillSetFromXMLString(java.util.Set set, org.dom4j.Element el, ims.domain.DomainFactory factory, java.util.HashMap domMap) throws Exception
	{
		if (el == null)
			return;
		
		java.util.List cl = el.elements("class");
		int size = cl.size();
		
		java.util.Set newSet = new java.util.HashSet();
		for(int i=0; i<size; i++) 
		{
			org.dom4j.Element itemEl = (org.dom4j.Element)cl.get(i);
			ManchesterTriageProtocolConfiguration domainObject = getManchesterTriageProtocolConfigurationfromXML(itemEl, factory, domMap);

			if (domainObject == null)
			{
				continue;
			}
			
			//Trying to avoid the hibernate collection being marked as dirty via its public interface methods. (like add)
			if (!set.contains(domainObject)) 
				set.add(domainObject);
			newSet.add(domainObject);			
		}
		
		java.util.Set removedSet = new java.util.HashSet();
		java.util.Iterator iter = set.iterator();
		//Find out which objects need to be removed
		while (iter.hasNext())
		{
			ims.domain.DomainObject o = (ims.domain.DomainObject)iter.next();			
			if ((o == null || o.getIsRIE() == null || !o.getIsRIE().booleanValue()) && !newSet.contains(o))
			{
				removedSet.add(o);
			}
		}
		iter = removedSet.iterator();
		//Remove the unwanted objects
		while (iter.hasNext())
		{
			set.remove(iter.next());
		}		
	}
	
	private static void fillListFromXMLString(java.util.List list, org.dom4j.Element el, ims.domain.DomainFactory factory, java.util.HashMap domMap) throws Exception
	{
		if (el == null)
			return;
		
		java.util.List cl = el.elements("class");
		int size = cl.size();
		
		for(int i=0; i<size; i++) 
		{
			org.dom4j.Element itemEl = (org.dom4j.Element)cl.get(i);
			ManchesterTriageProtocolConfiguration domainObject = getManchesterTriageProtocolConfigurationfromXML(itemEl, factory, domMap);

			if (domainObject == null)
			{
				continue;
			}

			int domIdx = list.indexOf(domainObject);
			if (domIdx == -1)
			{
				list.add(i, domainObject);
			}
			else if (i != domIdx && i < list.size())
			{
				Object tmp = list.get(i);
				list.set(i, list.get(domIdx));
				list.set(domIdx, tmp);
			}
		}		
		
		//Remove all ones in domList where index > voCollection.size() as these should
		//now represent the ones removed from the VO collection. No longer referenced.
		int i1=list.size();
		while (i1 > size)
		{
			list.remove(i1-1);
			i1=list.size();
		}
	}
		
	public static ManchesterTriageProtocolConfiguration getManchesterTriageProtocolConfigurationfromXML(String xml, ims.domain.DomainFactory factory, java.util.HashMap domMap) throws Exception
	{
		org.dom4j.Document doc = new org.dom4j.io.SAXReader().read(new org.xml.sax.InputSource(xml));
		return getManchesterTriageProtocolConfigurationfromXML(doc.getRootElement(), factory, domMap);
	}
	
	public static ManchesterTriageProtocolConfiguration getManchesterTriageProtocolConfigurationfromXML(org.dom4j.Element el, ims.domain.DomainFactory factory, java.util.HashMap domMap) throws Exception
	{
		if (el == null)
			return null;
		
		String className = el.attributeValue("type");
		if (!ManchesterTriageProtocolConfiguration.class.getName().equals(className))
		{
			Class clz = Class.forName(className);
			if (!ManchesterTriageProtocolConfiguration.class.isAssignableFrom(clz))
				throw new Exception("Element of type = " + className + " cannot be imported using the ManchesterTriageProtocolConfiguration class");
			String shortClassName = className.substring(className.lastIndexOf(".")+1);
			String methodName = "get" + shortClassName + "fromXML";
			java.lang.reflect.Method m = clz.getMethod(methodName, new Class[]{org.dom4j.Element.class, ims.domain.DomainFactory.class, java.util.HashMap.class});
			return (ManchesterTriageProtocolConfiguration)m.invoke(null, new Object[]{el, factory, domMap});
		}

		String impVersion = el.attributeValue("classVersion");
		if(!impVersion.equals(ManchesterTriageProtocolConfiguration.CLASSVERSION))
		{
			throw new Exception("Incompatible class structure found. Cannot import instance.");
		}		
		
		ManchesterTriageProtocolConfiguration ret = null;
		int extId = Integer.parseInt(el.attributeValue("id"));
		String externalSource = el.attributeValue("source");
		ret = (ManchesterTriageProtocolConfiguration)factory.getImportedDomainObject(ManchesterTriageProtocolConfiguration.class, externalSource, extId);	
		if (ret == null)
		{
			ret = new ManchesterTriageProtocolConfiguration();
		}
		String keyClassName = "ManchesterTriageProtocolConfiguration";

		ims.configuration.ImportedObject impObj = (ims.configuration.ImportedObject)domMap.get(keyClassName + "_" + externalSource + "_" + extId);
		if (impObj != null)
		{
			return (ManchesterTriageProtocolConfiguration)impObj.getDomainObject();
		}
		else
		{
    		impObj = new ims.configuration.ImportedObject();
    		impObj.setExternalId(extId);
    		impObj.setExternalSource(externalSource);
    		impObj.setDomainObject(ret);
			domMap.put(keyClassName + "_" + externalSource + "_" + extId, impObj);
		}
		fillFieldsfromXML(el, factory, ret, domMap);
		return ret;
	}

	public static void fillFieldsfromXML(org.dom4j.Element el, ims.domain.DomainFactory factory, ManchesterTriageProtocolConfiguration obj, java.util.HashMap domMap) throws Exception
	{
		org.dom4j.Element fldEl;
		fldEl = el.element("problem");
		if(fldEl != null)
		{
			fldEl = fldEl.element("class");		
			obj.setProblem(ims.clinical.configuration.domain.objects.ClinicalProblem.getClinicalProblemfromXML(fldEl, factory, domMap)); 
		}
		fldEl = el.element("protocolName");
		if(fldEl != null)
		{	
    		obj.setProtocolName(new String(fldEl.getTextTrim()));	
		}
		fldEl = el.element("protocolDescription");
		if(fldEl != null)
		{	
    		obj.setProtocolDescription(new String(fldEl.getTextTrim()));	
		}
		fldEl = el.element("defaultPriority");
		if(fldEl != null)
		{
			fldEl = fldEl.element("lki");
			obj.setDefaultPriority(ims.domain.lookups.LookupInstance.fromXMLString(fldEl, factory)); 	
		}
		fldEl = el.element("activeStatus");
		if(fldEl != null)
		{
			fldEl = fldEl.element("lki");
			obj.setActiveStatus(ims.domain.lookups.LookupInstance.fromXMLString(fldEl, factory)); 	
		}
		fldEl = el.element("discriminators");
		if(fldEl != null)
		{
			fldEl = fldEl.element("list");	
			obj.setDiscriminators(ims.emergency.configuration.domain.objects.ProtocolDiscriminator.fromListXMLString(fldEl, factory, obj.getDiscriminators(), domMap));
		}
		fldEl = el.element("taxonomyMap");
		if(fldEl != null)
		{
			fldEl = fldEl.element("list");	
			obj.setTaxonomyMap(ims.core.clinical.domain.objects.TaxonomyMap.fromListXMLString(fldEl, factory, obj.getTaxonomyMap(), domMap));
		}
	}

	public static String[] getCollectionFields()
	{
		return new String[]{
		 "discriminators"
		, "taxonomyMap"
		};
	}


	public static class FieldNames	
	{
	public static final String ID = "id";
		public static final String Problem = "problem";
		public static final String ProtocolName = "protocolName";
		public static final String ProtocolDescription = "protocolDescription";
		public static final String DefaultPriority = "defaultPriority";
		public static final String ActiveStatus = "activeStatus";
		public static final String Discriminators = "discriminators";
		public static final String TaxonomyMap = "taxonomyMap";
	}
}
