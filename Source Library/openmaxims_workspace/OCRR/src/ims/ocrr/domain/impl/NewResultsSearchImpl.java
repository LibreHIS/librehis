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
// This code was generated by Cristian Belciug using IMS Development Environment (version 1.80 build 4785.23502)
// Copyright (C) 1995-2013 IMS MAXIMS. All rights reserved.

package ims.ocrr.domain.impl;

import ims.configuration.gen.ConfigFlag;
import ims.core.patient.domain.objects.Patient;
import ims.core.patient.vo.PatientRefVo;
import ims.core.resource.place.vo.ClinicRefVo;
import ims.core.resource.place.vo.LocationRefVo;
import ims.core.vo.HcpLiteVo;
import ims.core.vo.PatientShort;
import ims.core.vo.ServiceLiteVo;
import ims.core.vo.ServiceLiteVoCollection;
import ims.core.vo.domain.PatientShortAssembler;
import ims.core.vo.lookups.LocationType;
import ims.core.vo.lookups.PatIdType;
import ims.domain.exceptions.DomainRuntimeException;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.interfaces.IAppRole;
import ims.framework.utils.Date;
import ims.ocrr.domain.SelectandOrder;
import ims.ocrr.domain.base.impl.BaseNewResultsSearchImpl;
import ims.ocrr.vo.NewResultsCriteriaVo;
import ims.ocrr.vo.NewResultsSearchListVoCollection;
import ims.ocrr.vo.RoleDisciplineSecurityLevelLiteGCVo;
import ims.ocrr.vo.domain.NewResultsSearchListVoAssembler;
import ims.ocrr.vo.lookups.OrderInvStatus;
import ims.ocrr.vo.lookups.OrderInvStatusCollection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewResultsSearchImpl extends BaseNewResultsSearchImpl
{
	private static final long serialVersionUID = 1L;
	private static final int PATIENT = 6;

	public NewResultsSearchListVoCollection listNewResults(NewResultsCriteriaVo criteria)
	{
		if (criteria == null || criteria.getFromDate() == null)
			throw new CodingRuntimeException("Invalid start date");

		// Search for OrderInvestigation ID matching the search criteria by intervals
		List<Integer> investigations = getInvestigationsByCriteria(criteria);
		

		// If no OrderInvestigation ID was found in the interval, then return null
		if (investigations == null || investigations.size() == 0)
			return null;
		
		
//		StringBuilder query = new StringBuilder(getBaseSelectQuery(investigations));
		
		StringBuilder query = new StringBuilder("SELECT oi ");
		query.append(" FROM OrderInvestigation AS oi WHERE oi.id IN (");
		for (int i = 0; i < investigations.size(); i++)
		{
			query.append(investigations.get(i));
			if (i != investigations.size() - 1)
				query.append(", ");
		}
		query.append(")");
		

		query.append(" ORDER BY oi.resultSortDate");
		if (Boolean.TRUE.equals(criteria.getNewestFirst()))
			 query.append(" DESC");
		
		query.append(", oi.ordInvSeq");

		
		long startTime = System.currentTimeMillis();

		NewResultsSearchListVoCollection results = NewResultsSearchListVoAssembler.createNewResultsSearchListVoCollectionFromOrderInvestigation(getDomainFactory().find(query.toString()));

		long endTime = System.currentTimeMillis();

		System.out.println("Time elapsed to retrive record data (ms): " + (endTime - startTime));

		return results;
	}


	/**
	 * This function will retrieve only the ID for OrderInvestigations, by searching for intervals of hours
	 * @param criteria
	 * @param offsetHours 
	 * @return
	 * @throws SQLException 
	 */
	private List<Integer> getInvestigationsByCriteria(NewResultsCriteriaVo criteria)
	{
		if (criteria == null || criteria.getFromDate() == null)
			throw new CodingRuntimeException("Invalid parameter - Start Date");

		ArrayList<String> paramNames = new ArrayList<String>();
		ArrayList<Object> paramValues = new ArrayList<Object>();
			
		List<Integer> investigationsID = new ArrayList<Integer>();
		
		
		String query = buildFilterQuery(criteria, paramNames, paramValues);
		
		long startTime = System.currentTimeMillis();
			
		
		List<?> results = getDomainFactory().find(query, paramNames, paramValues, ConfigFlag.DOM.NEW_RESULTS_SEARCH_MAX_SIZE.getValue());
		
		if (results != null)
		{
			for (int i = 0; i < results.size(); i++)
			{
				if (results.get(i) instanceof Integer)
					investigationsID.add((Integer) results.get(i));
			}
		}

		
		long endTime = System.currentTimeMillis();
		
		System.out.println("Elapsed Time (ms): " + (endTime - startTime));
		
		return investigationsID;
	}

	private String buildFilterQuery(NewResultsCriteriaVo criteria, ArrayList<String> paramNames, ArrayList<Object> paramValues)
	{
		boolean edOrders = Boolean.TRUE.equals(criteria.getEDOrders());
		boolean inpatientOrd = Boolean.TRUE.equals(criteria.getInpatientOrders()) || Boolean.TRUE.equals(criteria.getCurrentInpatientsOnly()) || Boolean.TRUE.equals(criteria.getInpatientOrdersPatientDischarged()) || Boolean.TRUE.equals(criteria.getExcludeCurrentInpatients());
		boolean outpatientOrd = Boolean.TRUE.equals(criteria.getOutpatientOrders()) || criteria.getOutpatientDepartment() != null;
		boolean searchAfterHosptal = !Boolean.TRUE.equals(criteria.getCurrentInpatientsOnly()) 
										&& !Boolean.TRUE.equals(criteria.getInpatientOrdersPatientDischarged()) 
										&& !edOrders 
										&& criteria.getHospital() != null 
										&& criteria.getOrderingLocation() == null;

		StringBuilder query = new StringBuilder("SELECT oi.id ");
		query.append("FROM OrderInvestigation AS oi ");
		query.append("JOIN oi.ordInvCurrentStatus.ordInvStatus AS status ");
			

	// 
		if (inpatientOrd || edOrders || outpatientOrd || (Boolean.TRUE.equals(criteria.getCurrentInpatientsOnly()) || Boolean.TRUE.equals(criteria.getInpatientOrdersPatientDischarged()) || Boolean.TRUE.equals(criteria.getExcludeCurrentInpatients())) || (criteria.getHCP() instanceof HcpLiteVo && Boolean.TRUE.equals(criteria.getCurrenIP()))
				|| criteria.getOrderingHCP() != null || criteria.getSelectedTab().equals(PATIENT))
			query.append(" JOIN oi.orderDetails AS ordSession ");
		
		if(criteria.getDiscipline() != null && criteria.getDiscipline().size() > 0)
			query.append(" JOIN oi.investigation AS inv ");
		
		if(inpatientOrd || edOrders)
			query.append(" LEFT JOIN oi.patientLocation AS pl LEFT JOIN pl.parentLocation AS plParentLocation ");
		
		if((Boolean.TRUE.equals(criteria.getCurrentInpatientsOnly()) || Boolean.TRUE.equals(criteria.getInpatientOrdersPatientDischarged()) || Boolean.TRUE.equals(criteria.getExcludeCurrentInpatients()))
				|| (criteria.getHCP() instanceof HcpLiteVo && Boolean.TRUE.equals(criteria.getCurrenIP())) || criteria.getSelectedTab().equals(PATIENT) )
			query.append(" JOIN ordSession.patient AS ordPat LEFT JOIN ordPat.ward AS ward LEFT JOIN ward.type AS type ");
		
		if(outpatientOrd)
			query.append(" LEFT JOIN oi.patientClinic AS pc LEFT JOIN ordSession.outpatientDept AS outDept ");
		
		if(Boolean.TRUE.equals(criteria.getAbnormalPathologyResultsOnly()))
			query.append(" LEFT JOIN oi.resultDetails AS resultDetails LEFT JOIN resultDetails.pathologyResultDetails AS pathologyResultsDetails ");

		if(criteria.getHCP() instanceof HcpLiteVo && Boolean.TRUE.equals(criteria.getCurrenIP())) //WDEV-15901
		{
			HcpLiteVo hcp = (HcpLiteVo) criteria.getHCP();
			if (hcp.getID_Hcp() == null)
				throw new DomainRuntimeException("Invalid Clinician");

			query.append(" LEFT JOIN ordPat.currentResponsibleConsultant AS cons ");
		}
		
		query.append("WHERE ");


	// Date condition
		query.append("oi.resultSortDate BETWEEN :FROM_DATE AND :TO_DATE ");
		query.append(" AND ");
		
		paramNames.add("FROM_DATE");	paramValues.add(criteria.getFromDate().getDate());
		paramNames.add("TO_DATE");		paramValues.add(new Date(criteria.getToDate()).addDay(1).getDate());
			
	
	// Status condition
		OrderInvStatusCollection requiredStatus = getRequiredInvStatuses(criteria);
		
		
		query.append("(status.id IN (");
		for (int i = 0; i < requiredStatus.size(); i++)
		{
			query.append(requiredStatus.get(i).getId());
			if (i != requiredStatus.size() - 1) query.append(", ");		// Not the last element
		}
		query.append(")");
		
		// If the result was marked for Review and Updated - it should still be retrieved when searching for results marked for Review
		if (requiredStatus.contains(OrderInvStatus.REVIEW))
		{
			/* TODO MSSQL case - query.append(" OR oi.forReview = 1"); */
			query.append(" OR oi.forReview = TRUE");
		}
		
		query.append(")");
		
		if (Boolean.TRUE.equals(criteria.getAbnormalPathologyResultsOnly()))
		{
			/* TODO MSSQL case - query.append(" AND pathologyResultsDetails.isAbnormal = 1 "); */
			query.append(" AND pathologyResultsDetails.isAbnormal = TRUE ");
		}	

		String condition = " AND ";
		
		// Search criteria
		String openBracket = "";
		
		String conditionalOperator = " "; 
		
		if (criteria.getPatientId() != null)
		{
			query.append(condition);
			query.append(" (ordPat.id = :patientId)");
			paramNames.add("patientId");
			paramValues.add(criteria.getPatientId());
				
			condition = " AND ";
			conditionalOperator = " OR ";
		}
		
		if (searchAfterHosptal)
		{
			query.append(openBracket);
				
			query.append(condition);
			query.append(" (oi.parentLocation.id = :Hospital)");
			paramNames.add("Hospital");
			paramValues.add(criteria.getHospital().getID_Location());
				
			condition = " AND ";
				
			openBracket = " ";
			conditionalOperator = " OR ";
		}
		
		if (!Boolean.TRUE.equals(criteria.getInpatientOrders()) && !Boolean.TRUE.equals(criteria.getOutpatientOrders()) && !Boolean.TRUE.equals(criteria.getCurrentInpatientsOnly()) && !Boolean.TRUE.equals(criteria.getInpatientOrdersPatientDischarged()))
		{
			if(criteria.getOrderingLocation() != null && criteria.getOrderingLocation() instanceof LocationRefVo && criteria.getOutpatientDepartment() == null)
			{
				LocationRefVo locRef = (LocationRefVo)criteria.getOrderingLocation();
				
				query.append(openBracket);
				
				query.append(condition);
				query.append(" oi.patientLocation.id = :location");
				paramNames.add("location");
				paramValues.add(locRef.getID_Location());
				
				condition = " AND ";
				
				openBracket = " ";
				conditionalOperator = " OR ";
			}
			else if (criteria.getOrderingLocation() != null && criteria.getOrderingLocation() instanceof ClinicRefVo)
			{
				ClinicRefVo clinRef = (ClinicRefVo)criteria.getOrderingLocation();
				
				query.append(openBracket);
				
				query.append(condition);
				query.append(" oi.patientClinic.id = :clinic");
				paramNames.add("clinic");
				paramValues.add(clinRef.getID_Clinic());
				
				condition = " AND ";
				
				openBracket = " ";
				conditionalOperator = " OR ";
 			}
		}
		boolean isNeededInpatientAndOutpatientBracket = false;
		boolean didAddInpatientAndOutpatientBracket = false;
		
		if((Boolean.TRUE.equals(criteria.getInpatientOrders()) || Boolean.TRUE.equals(criteria.getCurrentInpatientsOnly()) || Boolean.TRUE.equals(criteria.getInpatientOrdersPatientDischarged()) || Boolean.TRUE.equals(criteria.getExcludeCurrentInpatients())) && Boolean.TRUE.equals(criteria.getOutpatientOrders()))
		{
			isNeededInpatientAndOutpatientBracket = true;
		}
		
		if (Boolean.TRUE.equals(criteria.getInpatientOrders()))
		{
			query.append(openBracket);
			query.append(condition);
			
			if(isNeededInpatientAndOutpatientBracket && !didAddInpatientAndOutpatientBracket)
			{
				query.append(" (( ");
				didAddInpatientAndOutpatientBracket = true;
			}
			
			query.append(" pl is not null AND pl.type.id = :WARD_TYPE ");
			
			paramNames.add("WARD_TYPE");
			paramValues.add(LocationType.WARD.getID());
			
			if(criteria.getOrderingLocation() != null && criteria.getOrderingLocation() instanceof LocationRefVo && criteria.getOutpatientDepartment() == null)
			{
				query.append(" and pl.id = :InpatientOrderWard ");
				paramNames.add("InpatientOrderWard");
				paramValues.add(((LocationRefVo)criteria.getOrderingLocation()).getID_Location());
			}
			
			condition = " AND ";
			
			openBracket = " ";
			conditionalOperator = " OR ";
		}
		
		if (Boolean.TRUE.equals(criteria.getCurrentInpatientsOnly()))
		{
			query.append(openBracket);
			
			query.append(condition);
			
			if(isNeededInpatientAndOutpatientBracket && !didAddInpatientAndOutpatientBracket)
			{
				query.append(" (( ");
				didAddInpatientAndOutpatientBracket = true;
			}
			
			query.append(" (");
			query.append(" (ward is not null ");
			
			if(criteria.getInpatientWard() != null)
			{
				query.append(" and ward.id = :InpatientWard ");
				paramNames.add("InpatientWard");
				paramValues.add(criteria.getInpatientWard().getID());
			}
			else if(criteria.getInpatientHospital() != null)
			{
				query.append(" and ward.parentLocation.id = :HospitalInpatient ");
				paramNames.add("HospitalInpatient");
				paramValues.add(criteria.getInpatientHospital().getID());
			}
			
			query.append(") ");
			
			condition = " AND ";
			
			openBracket = " ";
			conditionalOperator = " OR ";
		}
		if(Boolean.TRUE.equals(criteria.getInpatientOrdersPatientDischarged()))
		{
			query.append(openBracket);
			
			if(Boolean.TRUE.equals(criteria.getCurrentInpatientsOnly()))
			{
				query.append(conditionalOperator);
			}
			else
			{
				query.append(condition);
			}
			
			if(isNeededInpatientAndOutpatientBracket && !didAddInpatientAndOutpatientBracket)
			{
				query.append(" (( ");
				didAddInpatientAndOutpatientBracket = true;
			}
			
			query.append(" (ward is null and pl is not null AND pl.type.id = :WARDTYPE ");
			paramNames.add("WARDTYPE");
			paramValues.add(LocationType.WARD.getID());
			
			if(criteria.getInpatientWard() != null)
			{
				query.append(" and pl.id = :InpatientW ");
				paramNames.add("InpatientW");
				paramValues.add(criteria.getInpatientWard().getID());
			}
			else if(criteria.getInpatientHospital() != null)
			{
				query.append(" and plParentLocation.id = :HospitalI ");
				paramNames.add("HospitalI");
				paramValues.add(criteria.getInpatientHospital().getID());
			}
			
			query.append(") ");
			
			condition = " AND ";
			
			openBracket = " ";
			conditionalOperator = " OR ";
		}
		if (Boolean.TRUE.equals(criteria.getCurrentInpatientsOnly()))
		{
			query.append(" ) ");
		}
		
		if (Boolean.TRUE.equals(criteria.getExcludeCurrentInpatients()))
		{
			query.append(openBracket);
			
			query.append(condition);
			
			if(isNeededInpatientAndOutpatientBracket && !didAddInpatientAndOutpatientBracket)
			{
				query.append(" (( ");
				didAddInpatientAndOutpatientBracket = true;
			}
			
			query.append(" ward is null");
			
			condition = " AND ";
			
			openBracket = " ";
			conditionalOperator = " OR ";
		}
		
		if(isNeededInpatientAndOutpatientBracket)
		{
			query.append(" ) ");
		}
		
		if (Boolean.TRUE.equals(criteria.getOutpatientOrders()))
		{
			query.append(openBracket);
			
			
			if(isNeededInpatientAndOutpatientBracket)
			{
				query.append(conditionalOperator);
			}
			else
			{
				query.append(condition);
			}
			
			query.append(" ((pc is not null or outDept is not null) ");
			
			if (criteria.getOrderingLocation() != null && criteria.getOrderingLocation() instanceof ClinicRefVo)
			{
				query.append(" and pc.id = :OutpatientOrdersClinic ");
				paramNames.add("OutpatientOrdersClinic");
				paramValues.add(((ClinicRefVo)criteria.getOrderingLocation()).getID_Clinic());
			}
			else if(criteria.getOutpatientDepartment() != null)
			{
				query.append(" and (pc.outpatientDept.id = :OutPatientD or outDept.id = :OutPatientD) ");
				paramNames.add("OutPatientD");
				paramValues.add(criteria.getOutpatientDepartment().getID());
			}
			
			query.append(") ");
			
			condition = " AND ";
			
			openBracket = " ";
			conditionalOperator = " OR ";
		}
		
		
		if(isNeededInpatientAndOutpatientBracket)
		{
			query.append(" ) ");
		}
		
		if (Boolean.TRUE.equals(criteria.getEDOrders()))
		{
			query.append(openBracket);
			query.append(condition);
			
			query.append(" pl is not null AND pl.type.id = :ED_TYPE ");
			
			paramNames.add("ED_TYPE");
			paramValues.add(LocationType.ANE.getID());
			
			condition = " AND ";
			
			openBracket = " ";
			conditionalOperator = " OR ";
		}
		
		if(criteria.getDiscipline() != null && criteria.getDiscipline().size() > 0)
		{
			String disciplineList = getDisciplineList(criteria.getDiscipline());
			
			if(disciplineList != null)
			{
				query.append(openBracket);
				
				query.append(condition);
				query.append(" inv.providerService.locationService.service.id in (" + disciplineList + ")");
				
				condition = " AND ";
				
				openBracket = " ";
				conditionalOperator = " OR ";
			}
		}
		
		if (criteria.getOutpatientDepartment() != null && !Boolean.TRUE.equals(criteria.getOutpatientOrders()))
		{
			query.append(" AND (pc.outpatientDept.id = :OutPatientDept OR outDept.id = :OutPatientDept) ");
			paramNames.add("OutPatientDept");
			paramValues.add(criteria.getOutpatientDepartment().getID());
		}
		
		if(criteria.getHCP() != null || criteria.getOrderingHCP() != null || criteria.getReviewingHCP() != null || criteria.getSpecialty() != null)
		{
			query.append(" AND ( ");
			conditionalOperator = " "; 
			
			// Current Reponsible HCP
			if(criteria.getHCP() instanceof HcpLiteVo && Boolean.TRUE.equals(criteria.getCurrenIP())) //WDEV-15901
			{
				HcpLiteVo hcp = (HcpLiteVo)criteria.getHCP();
				if(hcp.getID_Hcp() == null)
					throw new DomainRuntimeException("Invalid Clinician");
				
				query.append(" (type.id = :WARD_TYPE AND cons.id = :RESPONSIBLE_CLINICIAN) ");
				
				paramNames.add("WARD_TYPE");
				paramNames.add("RESPONSIBLE_CLINICIAN");
				paramValues.add(LocationType.WARD.getID());
				paramValues.add(hcp.getID_Hcp());
				
				openBracket = " ";
				conditionalOperator = " OR ";
			}
			
			if(criteria.getHCP() instanceof HcpLiteVo && Boolean.TRUE.equals(criteria.getOrder()))
			{
				HcpLiteVo hcp = (HcpLiteVo)criteria.getHCP();
				if(hcp.getID_Hcp() == null)
					throw new DomainRuntimeException("Invalid clinician");
				
				query.append(openBracket);
				
				query.append(conditionalOperator);
				query.append(" oi.responsibleClinician.id = :hcp");
				paramNames.add("hcp");			
				paramValues.add(hcp.getID_Hcp());
				
				openBracket = " ";
				conditionalOperator = " OR ";
			}	
					
			if(criteria.getOrderingHCP() != null)
			{
				query.append(openBracket);
				
				query.append(conditionalOperator);
				query.append(" (ordSession.orderedBy is not null AND ordSession.orderedBy.id = :orderedById) ");
				paramNames.add("orderedById");
				paramValues.add(criteria.getOrderingHCP().getIMosId());
				
				openBracket = " ";
				conditionalOperator = " OR ";
			}
			
			
			if (criteria.getReviewingHCP() != null)
			{
				query.append(conditionalOperator);
				query.append(" oi.allocatedHCPforReview.id = ").append(criteria.getReviewingHCP().getID_Hcp());
				
				openBracket = " ";
				conditionalOperator = " OR ";
			}
			
			if(criteria.getSpecialty() != null)
			{
				query.append(conditionalOperator);
				query.append(" (oi.responsibleClinician.id in (select m.id from Medic as m where m.specialty.id = :SpecialtyId)) ");
				paramNames.add("SpecialtyId");
				paramValues.add(criteria.getSpecialty().getID());
				
				conditionalOperator = " OR ";
			}
			
			query.append(" ) ");
		}
		
		
		query.append(" ORDER BY oi.resultSortDate");
		
		if (Boolean.TRUE.equals(criteria.getNewestFirst()))
			query.append(" DESC");
		
		return query.toString();
	}
	

	@SuppressWarnings("unused")
	private String getBaseSelectQuery(List<Integer> investigations)
	{
		int defaultPatientIdentifierType = getDefaultPatientIdentifier();

		StringBuilder selectQuery = new StringBuilder();

		selectQuery.append("SELECT new ims.ocrr.vo.NewResultSearchListVo(");
		selectQuery.append("oi.id, oi.version, oi.repDateTime, oi.repTimeSupplied, oi.allocatedDateForReview, ");
		selectQuery.append("oi.resultSortDate, ");
		selectQuery.append("specimen.id, specimen.version, ");

		/* TODO MSSQL case - selectQuery.append("(SELECT COUNT (spec.id) FROM OrderInvestigation AS ordIn LEFT JOIN ordIn.specimen AS spec WHERE ordIn.id = oi.id AND spec.colTimeFillerSupplied = 1) AS TIMFIL, "); */
		selectQuery.append("(SELECT COUNT (spec.id) FROM OrderInvestigation AS ordIn LEFT JOIN ordIn.specimen AS spec WHERE ordIn.id = oi.id AND spec.colTimeFillerSupplied = TRUE) AS TIMFIL, ");

		selectQuery.append("reviewingHcpMos.name.surname, reviewingHcpMos.name.forename, ");
		selectQuery.append("pat.id, pat.version, pat.name.surname, pat.name.forename, ");

		selectQuery.append("(SELECT MAX(ident.value) FROM Patient AS patient2 LEFT JOIN patient2.identifiers AS ident WHERE pat.id = patient2.id AND ident.type = ").append(defaultPatientIdentifierType).append(") AS PatientID, ");
		selectQuery.append("(SELECT MAX(ident2.value) FROM Patient AS patient3 LEFT JOIN patient3.identifiers AS ident2 WHERE pat.id = patient3.id AND ident2.type = ").append(PatIdType.HOSPNUM.getId()).append(") AS PatientID2, ");
		selectQuery.append("(SELECT MAX(ident3.value) FROM Patient AS patient4 LEFT JOIN patient4.identifiers AS ident3 WHERE pat.id = patient4.id AND ident3.type = ").append(PatIdType.NHSN.getId()).append(") AS PatientID3, ");
		
		selectQuery.append("invIndex.name, inv.eventType.id, category.id, ");
		selectQuery.append("oi.resultStatus.id, ");

		/* TODO MSSQL case - selectQuery.append("(SELECT COUNT (pathRes.id) FROM OrderInvestigation AS ordInv LEFT JOIN ordInv.resultDetails AS resDet LEFT JOIN resDet.pathologyResultDetails AS pathRes WHERE ordInv.id = oi.id AND pathRes.isAbnormal = 1) AS ABN_RES, "); */
		selectQuery.append("(SELECT COUNT (pathRes.id) FROM OrderInvestigation AS ordInv LEFT JOIN ordInv.resultDetails AS resDet LEFT JOIN resDet.pathologyResultDetails AS pathRes WHERE ordInv.id = oi.id AND pathRes.isAbnormal = TRUE) AS ABN_RES, ");

		selectQuery.append("orderInvStatus.id, oi.ordInvCurrentStatus.changeDateTime, oi.ordInvCurrentStatus.statusReason, ");
		selectQuery.append("service.serviceName, ");
		selectQuery.append("patLocation.name, patClinic.clinicName, outpatDepartment.name, ");
		selectQuery.append("respGP.name.surname, respGP.name.forename, respGpTitle.text, ");
		selectQuery.append("respClinMos.name.surname, respClinMos.name.forename, respClinTitle.text, ");
		selectQuery.append("service.id, service.version, secLevel.id, secLevel.version, secLevel.securityLevel, secLevel.securityLevelDescription ");
		selectQuery.append(") ");
		

		selectQuery.append("FROM ");
		selectQuery.append(" OrderInvestigation AS oi JOIN oi.orderDetails AS ordSession JOIN ordSession.patient AS pat ");
		selectQuery.append(" JOIN oi.investigation AS inv JOIN inv.investigationIndex AS invIndex");
		selectQuery.append(" JOIN invIndex.category AS category ");
		selectQuery.append(" LEFT JOIN oi.ordInvCurrentStatus.ordInvStatus AS orderInvStatus ");
		selectQuery.append(" LEFT JOIN oi.allocatedHCPforReview AS reviewingHCP LEFT JOIN reviewingHCP.mos AS reviewingHcpMos ");
		selectQuery.append(" LEFT JOIN oi.specimen AS specimen ");

		selectQuery.append(" LEFT JOIN inv.providerService AS provServ LEFT JOIN provServ.locationService AS locService LEFT JOIN locService.service AS service ");
		selectQuery.append(" LEFT JOIN invIndex.securityLevel AS secLevel ");
		selectQuery.append(" LEFT JOIN ordSession.patientLocation AS patLocation LEFT JOIN ordSession.patientClinic AS patClinic LEFT JOIN ordSession.outpatientDept AS outpatDepartment ");
		selectQuery.append(" LEFT JOIN ordSession.responsibleGp AS respGP LEFT JOIN respGP.name.title AS respGpTitle ");
		selectQuery.append(" LEFT JOIN ordSession.responsibleClinician AS respClinician ");
		selectQuery.append(" LEFT JOIN respClinician.mos AS respClinMos LEFT JOIN respClinMos.name.title AS respClinTitle");
		
		
		selectQuery.append(" WHERE oi.id IN (");
		for (int i = 0; i < investigations.size(); i++)
		{
			selectQuery.append(investigations.get(i));
			if (i != investigations.size() - 1)
				selectQuery.append(", ");
		}
		selectQuery.append(")");
				
		return selectQuery.toString();
	}
	
	

	private int getDefaultPatientIdentifier()
	{
		String flagValue = ConfigFlag.UI.DISPLAY_PATID_TYPE.getValue();

		if ("PKEY".equals(flagValue))
			return PatIdType.PKEY.getId();

		if ("HOSPNUM".equals(flagValue))
			return PatIdType.HOSPNUM.getId();

		if ("NHSN".equals(flagValue))
			return PatIdType.NHSN.getId();

		if ("PPSN".equals(flagValue))
			return PatIdType.PPSN.getID();

		if ("CHARTNUM".equals(flagValue))
			return PatIdType.CHARTNUM.getId();

		if ("NTPFNUM".equals(flagValue))
			return PatIdType.NTPFNUM.getId();

		if ("SENTNUM".equals(flagValue))
			return PatIdType.SENTNUM.getId();

		if ("DISTRICT".equals(flagValue))
			return PatIdType.DISTRICT.getId();

		if ("CASENUM".equals(flagValue))
			return PatIdType.CASENUM.getId();

		if ("EMPI".equals(flagValue))
			return PatIdType.EMPI.getId();

		if ("CLIENTID".equals(flagValue))
			return PatIdType.CLIENTID.getId();

		if ("GMSID".equals(flagValue))
			return PatIdType.GMSID.getId();

		if ("PASID".equals(flagValue))
			return PatIdType.PASID.getId();

		if ("PATNUM".equals(flagValue))
			return PatIdType.PATNUM.getId();

		if ("PXNUMBER".equals(flagValue))
			return PatIdType.PXNUMBER.getId();

		if ("MRNNUM".equals(flagValue))
			return PatIdType.MRNNUM.getId();

		return 0;
	}


	private String getDisciplineList(ServiceLiteVoCollection disciplines)
	{
		if (disciplines == null)
			return null;

		String disciplineList = "";
		for (ServiceLiteVo dis : disciplines)
		{
			if (dis == null)
				continue;

			disciplineList += disciplineList.length() == 0 ? dis.getID_Service() : "," + dis.getID_Service();
		}

		return disciplineList;
	}
	

	private OrderInvStatusCollection getRequiredInvStatuses(NewResultsCriteriaVo criteria)
	{
		OrderInvStatusCollection stColl = new OrderInvStatusCollection();

		if (criteria == null)
			return stColl;

		if (Boolean.TRUE.equals(criteria.getNewUpdated()))
		{
			stColl.add(OrderInvStatus.NEW_RESULT);
			stColl.add(OrderInvStatus.UPDATED_RESULT);
			stColl.add(OrderInvStatus.RESULTED);
		}
		if (Boolean.TRUE.equals(criteria.getForReview()))
		{
			stColl.add(OrderInvStatus.REVIEW);
		}
		if (Boolean.TRUE.equals(criteria.getSeenChecked()))
		{
			stColl.add(OrderInvStatus.SEEN);
		}

		if (Boolean.TRUE.equals(criteria.getChecked()))
		{
			stColl.add(OrderInvStatus.CHECKED);
		}

		if (Boolean.TRUE.equals(criteria.getCompleted()))
		{
			stColl.add(OrderInvStatus.COMPLETE);
		}

		return stColl;
	}
	

	public RoleDisciplineSecurityLevelLiteGCVo getRoleDisciplineSecurityLevels(IAppRole role)
	{
		SelectandOrder impl = (SelectandOrder) getDomainImpl(SelectandOrderImpl.class);
		return impl.getRoleDisciplineSecurityLevels(role);
	}
	

	public PatientShort getPatient(PatientRefVo patient)
	{
		if (patient == null || patient.getID_Patient() == null)
			return null;

		return PatientShortAssembler.create((Patient) getDomainFactory().getDomainObject(Patient.class, patient.getID_Patient()));
	}
}
