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
// This code was generated by Bogdan Tofei using IMS Development Environment (version 1.80 build 5127.24028)
// Copyright (C) 1995-2014 IMS MAXIMS. All rights reserved.

package ims.emergency.domain.impl;

import ims.core.admin.domain.objects.CareContext;
import ims.core.admin.domain.objects.EpisodeOfCare;
import ims.core.admin.vo.CareContextRefVo;
import ims.core.admin.vo.EmergencyAttendanceRefVo;
import ims.core.admin.vo.EpisodeOfCareRefVo;
import ims.core.patient.vo.PatientRefVo;
import ims.core.vo.CareContextShortVo;
import ims.core.vo.CubicleAllocationForAmendTimesVoCollection;
import ims.core.vo.EpisodeofCareShortVo;
import ims.core.vo.domain.CareContextShortVoAssembler;
import ims.core.vo.domain.CubicleAllocationForAmendTimesVoAssembler;
import ims.core.vo.domain.EpisodeofCareShortVoAssembler;
import ims.domain.DomainFactory;
import ims.domain.exceptions.StaleObjectException;
import ims.emergency.domain.EventHistory;
import ims.emergency.domain.base.impl.BaseEmergencyAttendanceTimeAmendmentsImpl;
import ims.emergency.domain.objects.Tracking;
import ims.emergency.domain.objects.TrackingAttendanceStatus;
import ims.emergency.vo.BedAvailabilityVoCollection;
import ims.emergency.vo.DNWStatusForAmendTimesVoCollection;
import ims.emergency.vo.EDTransferVoCollection;
import ims.emergency.vo.EmergencyAttendanceForTimeAmendmentsLiteVoCollection;
import ims.emergency.vo.ReferralToSpecialtyTeamForEventHistoryVoCollection;
import ims.emergency.vo.SeenByHcpForEventHistoryVoCollection;
import ims.emergency.vo.SentToXrayVoCollection;
import ims.emergency.vo.TrackingAttendanceStatusForEventHistoryVo;
import ims.emergency.vo.TrackingAttendanceStatusForEventHistoryVoCollection;
import ims.emergency.vo.TrackingAttendanceStatusRefVo;
import ims.emergency.vo.TrackingForTimeAmendmentsVo;
import ims.emergency.vo.TrackingMovementTimesForEventHistoryVoCollection;
import ims.emergency.vo.TrackingRefVo;
import ims.emergency.vo.domain.BedAvailabilityVoAssembler;
import ims.emergency.vo.domain.DNWStatusForAmendTimesVoAssembler;
import ims.emergency.vo.domain.EDTransferVoAssembler;
import ims.emergency.vo.domain.EmergencyAttendanceForTimeAmendmentsLiteVoAssembler;
import ims.emergency.vo.domain.SentToXrayVoAssembler;
import ims.emergency.vo.domain.TrackingAttendanceStatusForEventHistoryVoAssembler;
import ims.emergency.vo.domain.TrackingForTimeAmendmentsVoAssembler;
import ims.emergency.vo.domain.TrackingMovementTimesForEventHistoryVoAssembler;
import ims.framework.FormName;
import ims.framework.exceptions.CodingRuntimeException;

import java.util.ArrayList;
import java.util.List;

public class EmergencyAttendanceTimeAmendmentsImpl extends BaseEmergencyAttendanceTimeAmendmentsImpl
{
	private static final long serialVersionUID = 1L;

	public EmergencyAttendanceForTimeAmendmentsLiteVoCollection getPastEmergencyEpisodes(ims.core.patient.vo.PatientRefVo patientRef)
	{
		if (patientRef == null || patientRef.getID_Patient() == null)
		{
			throw new CodingRuntimeException("Cannot get EmergencyAttendances on null Patient Id ");
		}

		DomainFactory factory = getDomainFactory();

		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer();

		hql.append(" select attendance from EmergencyAttendance as attendance left join attendance.patient as pat where pat.id = :patID");
		markers.add("patID");
		values.add(patientRef.getID_Patient());
		
		hql.append(" order by attendance.arrivalDateTime desc");

		return EmergencyAttendanceForTimeAmendmentsLiteVoAssembler.createEmergencyAttendanceForTimeAmendmentsLiteVoCollectionFromEmergencyAttendance(factory.find(hql.toString(), markers, values));
	}

	public SeenByHcpForEventHistoryVoCollection listSeenByHcp(CareContextRefVo careContextRef)
	{
		EventHistory impl = (EventHistory)getDomainImpl(EventHistoryImpl.class);
		return impl.getSeenByHcp(careContextRef);
	}

	public ReferralToSpecialtyTeamForEventHistoryVoCollection listReferralsToSpecialty(CareContextRefVo careContextRef)
	{
		EventHistory impl = (EventHistory)getDomainImpl(EventHistoryImpl.class);
		return impl.getReferralsToSpecialty(careContextRef);
	}

	public TrackingAttendanceStatusForEventHistoryVoCollection listTrackingAttendanceStatuses(CareContextRefVo careContextRef)
	{
		if (careContextRef == null || careContextRef.getID_CareContext() == null)
		{
			throw new CodingRuntimeException("Cannot get TrackingAttendanceStatusForEventHistoryVoCollection on null Id for CareContext ");
		}

		DomainFactory factory = getDomainFactory();

		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer();

		hql.append(" select attendanceStatus from TrackingAttendanceStatus as attendanceStatus left join attendanceStatus.attendance as contextID where contextID.id = :contextID order by attendanceStatus.systemInformation.creationDateTime desc");
		markers.add("contextID");
		values.add(careContextRef.getID_CareContext());

		return TrackingAttendanceStatusForEventHistoryVoAssembler.createTrackingAttendanceStatusForEventHistoryVoCollectionFromTrackingAttendanceStatus(factory.find(hql.toString(), markers, values));
	}

	public TrackingMovementTimesForEventHistoryVoCollection listTrackingMovementTimes(CareContextRefVo careContextRef)
	{
		if (careContextRef == null || careContextRef.getID_CareContext() == null)
		{
			throw new CodingRuntimeException("Cannot get TrackingMovementTimesForEventHistoryVoCollection on null Id for CareContext ");
		}

		DomainFactory factory = getDomainFactory();

		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer();

		hql.append(" select movementTimes from TrackingMovementTimes as movementTimes left join movementTimes.attendance as contextID where contextID.id = :contextID order by movementTimes.systemInformation.creationDateTime desc");
		markers.add("contextID");
		values.add(careContextRef.getID_CareContext());

		return TrackingMovementTimesForEventHistoryVoAssembler.createTrackingMovementTimesForEventHistoryVoCollectionFromTrackingMovementTimes(factory.find(hql.toString(), markers, values));
	}

	public CubicleAllocationForAmendTimesVoCollection listCubicleAllocation(CareContextRefVo careContext)
	{
		if(careContext == null || careContext.getID_CareContext() == null)
			return null;
		
		String query = "select ca from CubicleAllocation as ca left join ca.attendance as cc where cc.id = :CareContextId order by ca.systemInformation.creationDateTime desc";
		List<?> list = getDomainFactory().find(query, new String[] {"CareContextId"}, new Object[] {careContext.getID_CareContext()});
		
		return CubicleAllocationForAmendTimesVoAssembler.createCubicleAllocationForAmendTimesVoCollectionFromCubicleAllocation(list);
	}

	public DNWStatusForAmendTimesVoCollection listDNW(CareContextRefVo careContextRef)
	{
		if(careContextRef == null || careContextRef.getID_CareContext() == null)
			return null;
	
		String query = "select dnw.currentStatus from DNW as dnw left join dnw.attendance as cc where cc.id = :CareContextId order by dnw.systemInformation.creationDateTime desc";
		List<?> list = getDomainFactory().find(query, new String[] {"CareContextId"}, new Object[] {careContextRef.getID_CareContext()});
	
		return DNWStatusForAmendTimesVoAssembler.createDNWStatusForAmendTimesVoCollectionFromDNWStatus(list);
	}

	public SentToXrayVoCollection listSentToXray(CareContextRefVo careContextRef)
	{
		if(careContextRef == null || careContextRef.getID_CareContext() == null)
			return null;
	
		String query = "select sx from SentToXray as sx left join sx.attendance as cc where cc.id = :CareContextId order by sx.systemInformation.creationDateTime desc";
		List<?> list = getDomainFactory().find(query, new String[] {"CareContextId"}, new Object[] {careContextRef.getID_CareContext()});
	
		return SentToXrayVoAssembler.createSentToXrayVoCollectionFromSentToXray(list);
	}

	public EDTransferVoCollection listTransfers(CareContextRefVo careContext)
	{
		if(careContext == null || careContext.getID_CareContext() == null)
			return null;
	
		String query = "select edt from EDTransfer as edt left join edt.attendance as cc where cc.id = :CareContextId order by edt.systemInformation.creationDateTime desc";
		List<?> list = getDomainFactory().find(query, new String[] {"CareContextId"}, new Object[] {careContext.getID_CareContext()});
	
		return EDTransferVoAssembler.createEDTransferVoCollectionFromEDTransfer(list);
	}

	public BedAvailabilityVoCollection listBedAvailability(CareContextRefVo careContext)
	{
		if(careContext == null || careContext.getID_CareContext() == null)
			return null;
	
		String query = "select bed from BedAvailability as bed left join bed.attendance as cc where cc.id = :CareContextId order by bed.systemInformation.creationDateTime desc";
		List<?> list = getDomainFactory().find(query, new String[] {"CareContextId"}, new Object[] {careContext.getID_CareContext()});
	
		return BedAvailabilityVoAssembler.createBedAvailabilityVoCollectionFromBedAvailability(list);
	}

	public Boolean isStale(TrackingAttendanceStatusRefVo patientStatus)
	{
		if (patientStatus == null || patientStatus.getID_TrackingAttendanceStatus() == null)
		{
			throw new CodingRuntimeException("Cannot get TrackingAttendanceStatusRefVo on null Id ");
		}

		DomainFactory factory = getDomainFactory();
		TrackingAttendanceStatus domainTrackingAttendanceStatus = (TrackingAttendanceStatus) factory.getDomainObject(TrackingAttendanceStatus.class, patientStatus.getID_TrackingAttendanceStatus());

		if (domainTrackingAttendanceStatus == null)
		{
			return true;
		}

		if (domainTrackingAttendanceStatus.getVersion() > patientStatus.getVersion_TrackingAttendanceStatus())
		{
			return true;
		}

		return false;
	}

	public TrackingForTimeAmendmentsVo getTracking(EmergencyAttendanceRefVo attendance)
	{
		if(attendance == null || attendance.getID_EmergencyAttendance() == null)
			return null;
	
		String query = "select tr from Tracking as tr left join tr.attendance as att where att.id = :AttendanceId";
		List<?> list = getDomainFactory().find(query, new String[] {"AttendanceId"}, new Object[] {attendance.getID_EmergencyAttendance()});
	
		if(list == null || list.size() == 0)
			return null;
		
		return TrackingForTimeAmendmentsVoAssembler.create((Tracking) list.get(0));
	}

	public void saveTrackingAndRIECurrentStatus(TrackingRefVo tracking, TrackingAttendanceStatusForEventHistoryVo status, FormName formName, PatientRefVo patient, CareContextRefVo careContext, String rieComment) throws StaleObjectException
	{
		if (tracking == null || tracking.getID_Tracking() == null)
			return;
		
		DomainFactory factory = getDomainFactory();

		/* TODO MSSQL case - String query = "select attendanceStatus from TrackingAttendanceStatus as attendanceStatus left join attendanceStatus.attendance as contextID where contextID.id = :ContextID and attendanceStatus.id <> :CurrentStatus and (attendanceStatus.isRIE is null or attendanceStatus.isRIE = 0) order by attendanceStatus.systemInformation.creationDateTime desc"; */
		String query = "select attendanceStatus from TrackingAttendanceStatus as attendanceStatus left join attendanceStatus.attendance as contextID where contextID.id = :ContextID and attendanceStatus.id <> :CurrentStatus and (attendanceStatus.isRIE is null or attendanceStatus.isRIE = FALSE) order by attendanceStatus.systemInformation.creationDateTime desc";

		TrackingAttendanceStatus prevStatus = (TrackingAttendanceStatus) factory.findFirst(query, new String[] {"ContextID", "CurrentStatus"}, new Object[] {careContext.getID_CareContext(), status.getID_TrackingAttendanceStatus()});
		
		if(prevStatus != null)
		{
			Tracking doTracking = (Tracking) factory.getDomainObject(Tracking.class, tracking.getID_Tracking());
			doTracking.setCurrentStatus(prevStatus);
			
			factory.save(doTracking);
			
			markAsRie(status, formName, (patient != null ? patient.getID_Patient() : null), null , (careContext != null ? careContext.getID_CareContext() : null), rieComment);
		}
	}

	public EpisodeofCareShortVo getEpisodeOfCare(EpisodeOfCareRefVo episodeOfCare)
	{
		if(episodeOfCare == null || episodeOfCare.getID_EpisodeOfCare() == null)
			throw new CodingRuntimeException("Cannnot get EOC for a null Id.");
		
		return EpisodeofCareShortVoAssembler.create((EpisodeOfCare) getDomainFactory().getDomainObject(EpisodeOfCare.class, episodeOfCare.getID_EpisodeOfCare()));
	}

	public CareContextShortVo getCareContext(CareContextRefVo careContext)
	{
		if(careContext == null || careContext.getID_CareContext() == null)
			throw new CodingRuntimeException("Cannot get CC for a null Id.");
	
		return CareContextShortVoAssembler.create((CareContext) getDomainFactory().getDomainObject(CareContext.class, careContext.getID_CareContext()));
	}
}
