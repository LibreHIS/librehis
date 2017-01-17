//#############################################################################
//#                                                                           #
//#  Copyright (C) <2015>  <IMS MAXIMS>                                       #
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
//#  IMS MAXIMS provides absolutely NO GUARANTEE OF THE CLINICAL SAFTEY of    #
//#  this program.  Users of this software do so entirely at their own risk.  #
//#  IMS MAXIMS only ensures the Clinical Safety of unaltered run-time        #
//#  software that it builds, deploys and maintains.                          #
//#                                                                           #
//#############################################################################
//#EOH
// This code was generated by Marius Mihalec using IMS Development Environment (version 1.45 build 2265.41281)
// Copyright (C) 1995-2006 IMS MAXIMS plc. All rights reserved.

package ims.ocrr.domain.impl;

import ims.core.charting.domain.objects.ChartType;
import ims.core.charting.vo.ChartTypeRefVo;
import ims.core.patient.vo.PatientRefVo;
import ims.core.resource.people.vo.HcpRefVo;
import ims.core.vo.ChartResultVo;
import ims.core.vo.ChartResultVoCollection;
import ims.core.vo.ChartTypeShortVoCollection;
import ims.core.vo.ChartTypeVo;
import ims.core.vo.ServiceLiteVo;
import ims.core.vo.ServiceLiteVoCollection;
import ims.core.vo.domain.ChartResultVoAssembler;
import ims.core.vo.domain.ChartTypeShortVoAssembler;
import ims.core.vo.domain.ChartTypeVoAssembler;
import ims.core.vo.domain.ServiceLiteVoAssembler;
import ims.core.vo.lookups.LocationType;
import ims.core.vo.lookups.PreActiveActiveInactiveStatus;
import ims.core.vo.lookups.ServiceCategory;
import ims.core.vo.lookups.TaxonomyType;
import ims.domain.DomainFactory;
import ims.domain.exceptions.DomainInterfaceException;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.interfaces.IAppRole;
import ims.framework.utils.Date;
import ims.framework.utils.DateTime;
import ims.framework.utils.Time;
import ims.ocrr.configuration.vo.InvestigationIndexRefVo;
import ims.ocrr.domain.ClinicalImagingResults;
import ims.ocrr.domain.SelectandOrder;
import ims.ocrr.domain.base.impl.BasePathologyResultsImpl;
import ims.ocrr.orderingresults.domain.objects.OrderInvestigation;
import ims.ocrr.orderingresults.domain.objects.OrderSpecimen;
import ims.ocrr.orderingresults.domain.objects.PathResultDetails;
import ims.ocrr.orderingresults.domain.objects.ResultComponent;
import ims.ocrr.orderingresults.vo.OrderInvestigationRefVo;
import ims.ocrr.orderingresults.vo.OrderSpecimenRefVo;
import ims.ocrr.vo.InvestigationIndexLiteVoCollection;
import ims.ocrr.vo.OrderInvestigationChartVo;
import ims.ocrr.vo.OrderSpecimenLabSpecCommentsVo;
import ims.ocrr.vo.PathologyResultListShortVoCollection;
import ims.ocrr.vo.RoleDisciplineSecurityLevelLiteGCVo;
import ims.ocrr.vo.SpecimenWorkListItemDateToCollectVoCollection;
import ims.ocrr.vo.domain.InvestigationIndexLiteVoAssembler;
import ims.ocrr.vo.domain.OrderInvestigationChartVoAssembler;
import ims.ocrr.vo.domain.OrderSpecimenLabSpecCommentsVoAssembler;
import ims.ocrr.vo.domain.PathologyResultListShortVoAssembler;
import ims.ocrr.vo.domain.SpecimenWorkListItemDateToCollectVoAssembler;
import ims.ocrr.vo.lookups.Category;
import ims.ocrr.vo.lookups.InvEventType;
import ims.ocrr.vo.lookups.OrderInvStatus;
import ims.ocrr.vo.lookups.ResultStatus;
import ims.ocrr.vo.lookups.ResultValueType;

import java.util.ArrayList;
import java.util.List;

public class PathologyResultsImpl extends BasePathologyResultsImpl
{
	private static final long serialVersionUID = 1L;

	private static final int IPOP_ALL			= 1;
	private static final int IPOP_INPATIENT		= 2;
	private static final int IPOP_OUTPATIENT	= 3;

	
	
	public ims.core.vo.HcpLiteVoCollection listClinicians(String name)
	{
		ClinicalImagingResults impl = (ClinicalImagingResults)getDomainImpl(ClinicalImagingResultsImpl.class);
		return impl.listClinicians(name);
	}
	public ims.ocrr.vo.InvestigationIndexLiteVoCollection listInvestigationTypes(String name)
	{
		if(name != null)
			name = name.toUpperCase();
		
		return InvestigationIndexLiteVoAssembler.createInvestigationIndexLiteVoCollectionFromInvestigationIndex(
				getDomainFactory().find("from InvestigationIndex as i1_1 where (i1_1.category = :category and i1_1.upperName like :name and i1_1.activeStatus = :status and i1_1.isProfile = :isProfile) order by i1_1.upperName", 
						new String[] {"category", "name", "status", "isProfile"}, new Object[] {getDomLookup(Category.PATHOLOGY), name, getDomLookup(PreActiveActiveInactiveStatus.ACTIVE), Boolean.FALSE}));
	}

	
	
	@SuppressWarnings({"rawtypes"})
	public PathologyResultListShortVoCollection listResults(PatientRefVo patient, Date fromDate, Date toDate, InvestigationIndexRefVo investigationType,
																ServiceLiteVo discipline, HcpRefVo clinician, Integer inpatientOutpatientOption, 
																Boolean resultsOnly, Boolean checked, Boolean unchecked) throws DomainInterfaceException
	{
		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();
		
		if(patient == null || patient.getID_Patient() == null)
			throw new CodingRuntimeException("Invalid patient");


		StringBuffer sb = new StringBuffer("from OrderInvestigation as o1_1 ");
		
		sb.append(" where o1_1.investigation.investigationIndex.category = :cat");
		markers.add("cat");
		values.add(getDomLookup(Category.PATHOLOGY));
		
		if(fromDate != null && toDate != null)
		{			
			sb.append(" and");
			sb.append(" o1_1.displayDateTime between :fromdate and :todate");
				
			markers.add("fromdate");
			values.add(new DateTime(fromDate, new Time(0,0,0)).getJavaDate());
			markers.add("todate");
			values.add(new DateTime(toDate, new Time(23,59,59)).getJavaDate());
		}
		else if (fromDate != null && toDate == null)
		{
			sb.append(" and");
			sb.append(" o1_1.displayDateTime >= :fromdate ");
				
			markers.add("fromdate");
			values.add(new DateTime(fromDate).getJavaDate());
		}
		else if (fromDate == null && toDate != null)
		{
			sb.append(" and");
			sb.append(" o1_1.displayDateTime <= :todate ");
				
			markers.add("todate");
			values.add(new DateTime(toDate, new Time(23,59,59)).getJavaDate());			
		}
		
		if (patient != null && patient.getID_PatientIsNotNull())
		{
			sb.append(" and");
			sb.append(" o1_1.orderDetails.patient.id  = :pid");
			markers.add("pid");
			values.add(patient.getID_Patient());
		}
		if(investigationType != null && investigationType.getID_InvestigationIndexIsNotNull())
		{
			sb.append(" and");
			sb.append(" o1_1.investigation.investigationIndex.id = :invtype");
			markers.add("invtype");
			values.add(investigationType.getID_InvestigationIndex());
		}		
		if(discipline != null && discipline.getID_ServiceIsNotNull())
		{
			sb.append(" and");
			sb.append(" o1_1.investigation.providerService.locationService.service.id = :discipline");
			markers.add("discipline");
			values.add(discipline.getID_Service());
		}
		if(clinician != null && clinician.getID_HcpIsNotNull())
		{
			sb.append(" and");
			sb.append(" o1_1.orderDetails.responsibleClinician.id = :clinician");
			markers.add("clinician");
			values.add(clinician.getID_Hcp());
		}
		
		
		switch (inpatientOutpatientOption)
		{
			case IPOP_ALL:
				break;
				
			case IPOP_INPATIENT:
				sb.append(" AND o1_1.patientLocation is not null AND o1_1.patientLocation.type = :WARD_TYPE ");
				markers.add("WARD_TYPE");
				values.add(getDomLookup(LocationType.WARD));
				break;
				
			case IPOP_OUTPATIENT:
				sb.append(" AND (o1_1.patientClinic is not null OR o1_1.orderDetails.outpatientDept is not null) " );
				break;
		}

		
		if (resultsOnly != null && resultsOnly.booleanValue())
		{
			sb.append(" and");
			sb.append(" (o1_1.resultDetails is not null ) ");//WDEV-10227
		}
		

		if ((checked != null && unchecked != null) /* LOGICAL XOR */
				&& ((checked && !unchecked) || (!checked && unchecked)))
		{
			if (checked)
			{
				sb.append(" AND o1_1.ordInvCurrentStatus.ordInvStatus = :CHECKED_STAT ");
				markers.add("CHECKED_STAT");
				values.add(getDomLookup(OrderInvStatus.CHECKED));
			}
			
			if (unchecked)
			{
				sb.append(" AND o1_1.ordInvCurrentStatus.ordInvStatus <> :CHECKED_STAT ");
				markers.add("CHECKED_STAT");
				values.add(getDomLookup(OrderInvStatus.CHECKED));
			}
		}
		
		
		sb.append(" order by o1_1.displayDateTime desc, o1_1.ordInvSeq asc, o1_1.systemInformation.creationDateTime, o1_1.systemInformation.creationUser");

		
		List l = getDomainFactory().find(sb.toString(), markers, values);		
		return PathologyResultListShortVoAssembler.createPathologyResultListShortVoCollectionFromOrderInvestigation(l);
	}	
	public ServiceLiteVoCollection listDiscipline()
	{
		/* TODO MSSQL case - return ServiceLiteVoAssembler.createServiceLiteVoCollectionFromService(
				getDomainFactory().find("from Service as srv where srv.isActive = 1 and srv.serviceCategory = :category order by srv.serviceName",
						new String[] { "category" },
						new Object[] { getDomLookup(ServiceCategory.PATHOLOGY_DISCIPLINE) })); */
		return ServiceLiteVoAssembler.createServiceLiteVoCollectionFromService(
				getDomainFactory().find("from Service as srv where srv.isActive = TRUE and srv.serviceCategory = :category order by srv.serviceName",
						new String[] { "category" }, 
						new Object[] { getDomLookup(ServiceCategory.PATHOLOGY_DISCIPLINE) }));
	}
	public ChartTypeShortVoCollection listChartTypes()
	{
		/* TODO MSSQL case - return ChartTypeShortVoAssembler.createChartTypeShortVoCollectionFromChartType(getDomainFactory().find("from ChartType as ct where ct.isActive = 1 order by ct.name")); */
		return ChartTypeShortVoAssembler.createChartTypeShortVoCollectionFromChartType(getDomainFactory().find("from ChartType as ct where ct.isActive = TRUE order by ct.name"));
	}
	@SuppressWarnings({"rawtypes", "unchecked"})
	public ChartResultVoCollection listChartResults(PatientRefVo patient, ChartTypeRefVo chartType, Date startDate, Date endDate, Boolean isTabularView) throws ims.domain.exceptions.DomainInterfaceException
	{
		if(patient == null || patient.getID_Patient() == null)
			throw new DomainInterfaceException("Invalid patient");
		if(chartType == null || chartType.getID_ChartType() == null)
			throw new DomainInterfaceException("Invalid profile");
		if(startDate == null)
			throw new DomainInterfaceException("Invalid start date");
		if(endDate == null)
			throw new DomainInterfaceException("Invalid end date");
		
		ArrayList markers = new ArrayList();
		ArrayList values = new ArrayList();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select r1_1, o2_1, specimen, p2_1 ");//WDEV-16232
		sb.append(" from OcsOrderSession as o1_1 join o1_1.patient as p1_1 join o1_1.investigations as o2_1 join o2_1.investigation as invinv join invinv.eventType as eType join o2_1.resultDetails as rd join rd.pathologyResultDetails as p2_1 join p2_1.resultComponents as r1_1 left join r1_1.analyte as a1_1 ");//WDEV-16232
		sb.append(" join p2_1.orderSpecimen AS specimen ");//WDEV-16232
		sb.append(" where ");
		sb.append(" (a1_1.id in (select distinct a1_1.id ");
		sb.append(" from ChartType as c1_1 left join c1_1.datasetTypes "); 
		sb.append(" as c2_1 left join c2_1.datasetType as d1_1, Analyte "); 
		sb.append(" as a1_1 left join a1_1.datasetType as d2_1 ");
		sb.append(" where ");
		sb.append(" (c1_1.id = :ChartTypeID)) ");
		sb.append(" and r1_1.obsDateTime between :StartDate and :EndDate and (r1_1.resValType = :NMResultType" + (Boolean.TRUE.equals(isTabularView) ? " or r1_1.resValType = :SNResultType or r1_1.resValType = :STResultType" : "") + ") and p1_1.id = :PatientID and eType.id <> :TimeSeries) ");//	WDEV-16232 	
		sb.append(" order by r1_1.obsDateTime desc ");
		
		markers.add("PatientID");
		values.add(patient.getID_Patient());
		markers.add("ChartTypeID");
		values.add(chartType.getID_ChartType());
		markers.add("StartDate");
		values.add(new DateTime(startDate, new Time(0,0,0)).getJavaDate());
		markers.add("EndDate");		
		values.add(new DateTime(endDate, new Time(23,59,59)).getJavaDate());
		markers.add("NMResultType");
		values.add(getDomLookup(ResultValueType.NM));	
		
		if(isTabularView)////WDEV-16232 
		{
			markers.add("SNResultType");
			values.add(getDomLookup(ResultValueType.SN));

			markers.add("STResultType");
			values.add(getDomLookup(ResultValueType.ST));
		}
		
		markers.add("TimeSeries");//WDEV-16232 
		values.add(InvEventType.TIME_SERIES.getID());	//WDEV-16232 
		
		List searchResult = getDomainFactory().find(sb.toString(), markers, values);
		
		if (searchResult == null || searchResult.size() == 0)
			return null;
		
		ChartResultVoCollection results = new ChartResultVoCollection();
		
		for (int i = 0; i < searchResult.size(); i++)
		{
			Object[] record = (Object[]) searchResult.get(i);
			
			if (record != null && record.length > 0)
			{
				ChartResultVo chartResult = ChartResultVoAssembler.create((ResultComponent) record[0]);
				
				if (record.length > 1 && record[1] != null)
				{
					OrderInvestigationChartVo orderInvestigation = OrderInvestigationChartVoAssembler.create((OrderInvestigation) record[1]);
					
					chartResult.setInvestigationData(orderInvestigation);
				}
				
				if (record.length > 2 && record[2] != null)
				{
					OrderSpecimenLabSpecCommentsVo labResults = OrderSpecimenLabSpecCommentsVoAssembler.create((OrderSpecimen) record[2]);
					chartResult.setSpecimenComments(labResults);
				}
				
				if (record.length > 3 && record[3] instanceof PathResultDetails)//WDEV-16232
				{
					chartResult.setPathologyResultId(((PathResultDetails) record[3]).getId());
				}
				
				results.add(chartResult);
			}
		}
		
		return results;
	}
	public ChartTypeVo getChartType(ChartTypeRefVo id)
	{
		return ChartTypeVoAssembler.create((ChartType)getDomainFactory().getDomainObject(ChartType.class, id.getID_ChartType()));
	}
	
	public InvestigationIndexLiteVoCollection listInvestigationTypesSynonyms(String name, Boolean beginWith) 
	{
				
		if(name != null)
		{
			name.replaceAll("%", "");
					
			if(beginWith != null)
				name = (!beginWith ? "%" + name.toUpperCase() + "%" : name.toUpperCase() + "%");
			else
				name = name.toUpperCase() + "%";
			
				
		}
		
		return InvestigationIndexLiteVoAssembler.createInvestigationIndexLiteVoCollectionFromInvestigationIndex(
				getDomainFactory().find("select distinct i1_1 from InvestigationIndex as i1_1 left join i1_1.synonyms as i2_1 where (i1_1.category = :category and ((i2_1.upperName like :name and i2_1.activeStatus = :Active) or i1_1.upperName like :name )and i1_1.activeStatus = :status and i1_1.isProfile = :isProfile) order by i1_1.upperName", 
						new String[] {"category", "name", "status", "isProfile", "Active"}, new Object[] {getDomLookup(Category.PATHOLOGY), name, getDomLookup(PreActiveActiveInactiveStatus.ACTIVE), Boolean.FALSE,Boolean.TRUE}));
	
	}
	
	//WDEV-11036
	public RoleDisciplineSecurityLevelLiteGCVo getRoleDisciplineSecurityLevels(IAppRole role)
	{
		SelectandOrder impl = (SelectandOrder)getDomainImpl(SelectandOrderImpl.class);
		return impl.getRoleDisciplineSecurityLevels(role);
	}
	@SuppressWarnings("rawtypes") //WDEV-11800
	public String[] getSystemReportAndTemplate(Integer imsId)
	{
		String[] result = null;
		
		DomainFactory factory = getDomainFactory();
		
		List lst = factory.find("select r1_1.reportXml, t1_1.templateXml, r1_1.reportName, r1_1.reportDescription, t1_1.name, t1_1.description from ReportBo as r1_1 left join r1_1.templates as t1_1 where (r1_1.imsId= :imsid) order by t1_1.name", new String[] {"imsid"}, new Object[] {imsId});
		
		if(lst.iterator().hasNext())
		{
			Object[] obj = (Object[])lst.iterator().next();
			
			result = new String[] {(String)obj[0], (String)obj[1], (String)obj[2], (String)obj[3], (String)obj[4], (String)obj[5]};
		}
		
		return result;
	}
	
	public Date getSpecimenWorkListItemDateToCollectBySpecimen(OrderSpecimenRefVo specimen)
	{
		if (specimen == null || specimen.getID_OrderSpecimen() == null)
			throw new CodingRuntimeException("specimen is null or id not provided in method getSpecimenWorkListItemDateToCollectBySpecimen");
			
		List date = getDomainFactory().find("select specw.dateToCollect from SpecimenWorkListItem as specw left join specw.specimen as spec where spec.id = " + specimen.getID_OrderSpecimen());
		if(date != null && date.size() > 0)
		{
			if(date.get(0) instanceof java.util.Date)
				return new Date((java.util.Date)date.get(0));
		}
		
		return null;
	}

	// WDEV-15709
	public String getMappingForResultStatusLookup(ResultStatus resultStatusInstance, TaxonomyType extSystem) 
	{
		if(resultStatusInstance == null || extSystem == null || extSystem.getText() == null)
			return null;
		
		String query = "select lm.extCode from LookupInstance as li left join li.mappings as lm where (li.id = :ResultStatusId and lm.extSystem = :TaxonomyType) ";
		
		List<?> list = getDomainFactory().find(query, new String[] {"ResultStatusId", "TaxonomyType"}, new Object[] {resultStatusInstance.getID(), extSystem.getText()});
		
		if(list != null && list.size() > 0)
		{
			if(list.get(0) instanceof String)
				return (String) list.get(0);
		}
		
		return null;
	}
	
	//WDEV-16232
	public Date getSpecimenWorkListItemDateToCollectByInvestigation(OrderInvestigationRefVo orderInvestigation) 
	{
		if (orderInvestigation == null || orderInvestigation.getID_OrderInvestigation() == null)
			throw new CodingRuntimeException("OrderInvestigation is null or id not provided in method getSpecimenWorkListItemDateToCollectByInvestigation");
			
		List<?> date = getDomainFactory().find("select specw.dateToCollect from SpecimenWorkListItem as specw left join specw.dFTOrderInvestigation as orderInv where orderInv.id = " + orderInvestigation.getID_OrderInvestigation());
		if(date != null && date.size() > 0)
		{
			if(date.get(0) instanceof java.util.Date)
				return new Date((java.util.Date)date.get(0));
		}
		
		return null;
	}
	
	//WDEV-18295
	public SpecimenWorkListItemDateToCollectVoCollection getSpecimensWithDateToCollect(String specimensIds)
	{
		
		List list = getDomainFactory().find("select specWorkItem from SpecimenWorkListItem as specWorkItem left join specWorkItem.specimen as spec where spec.id in ( " + specimensIds + " ) and specWorkItem.dateToCollect is not null ");
		
		return SpecimenWorkListItemDateToCollectVoAssembler.createSpecimenWorkListItemDateToCollectVoCollectionFromSpecimenWorkListItem(list);
	}
}
