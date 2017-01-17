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
// This code was generated by Florin Blindu using IMS Development Environment (version 1.80 build 5527.24259)
// Copyright (C) 1995-2015 IMS MAXIMS. All rights reserved.

package ims.RefMan.domain.impl;

import java.util.ArrayList;
import java.util.List;

import ims.RefMan.domain.CatsReferralRequestServiceDlg;
import ims.RefMan.domain.base.impl.BaseTransferOfCareImpl;
import ims.RefMan.vo.CareSpellForRequestServiceShortVo;
import ims.RefMan.vo.CatsReferralRefVo;
import ims.RefMan.vo.domain.CareSpellForRequestServiceShortVoAssembler;
import ims.core.admin.domain.objects.CareSpell;
import ims.core.configuration.vo.ContractConfigRefVo;
import ims.core.vo.HcpLiteVo;
import ims.core.vo.ServiceLiteVoCollection;
import ims.core.vo.domain.ServiceLiteVoAssembler;
import ims.domain.DomainFactory;
import ims.scheduling.vo.Booking_AppointmentRefVo;

public class TransferOfCareImpl extends BaseTransferOfCareImpl
{

	private static final long serialVersionUID = 1L;

	public ims.core.vo.ReferralSourceUrgencyVoCollection getSourceOfReferralConfigVoColl()
	{
		CatsReferralRequestServiceDlg impl =  (CatsReferralRequestServiceDlg)getDomainImpl(CatsReferralRequestServiceDlgImpl.class);
		return impl.getSourceOfReferralConfigVoColl();
	}

	public ims.RefMan.vo.CatsReferralMasterVo getParentCatsReferrall(ims.RefMan.vo.CatsReferralRefVo catsReferralRef)
	{
		CatsReferralRequestServiceDlg impl =  (CatsReferralRequestServiceDlg)getDomainImpl(CatsReferralRequestServiceDlgImpl.class);
		return impl.getParentCatsReferrall(catsReferralRef);
	}

	public ims.scheduling.vo.ExclusionDatesVo listExclusionDates()
	{
		CatsReferralRequestServiceDlg impl =  (CatsReferralRequestServiceDlg)getDomainImpl(CatsReferralRequestServiceDlgImpl.class);
		return impl.listExclusionDates();
	}

	public ims.RefMan.vo.ContractServiceLocationsConfigVo getContractServiceLocConfByContractService(ims.core.configuration.vo.ContractConfigRefVo contractRef, ims.core.clinical.vo.ServiceRefVo serviceRef)
	{
		CatsReferralRequestServiceDlg impl =  (CatsReferralRequestServiceDlg)getDomainImpl(CatsReferralRequestServiceDlgImpl.class);
		return impl.getContractServiceLocConfByContractService(contractRef, serviceRef);
	}

	public ims.framework.utils.Date getEndDateKPI(ims.RefMan.vo.CatsReferralForRequestServiceVo record)
	{
		CatsReferralRequestServiceDlg impl =  (CatsReferralRequestServiceDlg)getDomainImpl(CatsReferralRequestServiceDlgImpl.class);
		return impl.getEndDateKPI(record);
	}

	public HcpLiteVo getAppointmentSlotResponsable(Booking_AppointmentRefVo bookingAppointment)
	{
		CatsReferralRequestServiceDlg impl = (CatsReferralRequestServiceDlg)getDomainImpl(CatsReferralRequestServiceDlgImpl.class);
		return impl.getAppointmentSlotResponsable(bookingAppointment);
	}


	public CareSpellForRequestServiceShortVo getCareSpellForReferral(CatsReferralRefVo referral)
	{
		if (referral == null || referral.getID_CatsReferral() == null)
			return null;
		
		ArrayList<String> paramNames = new ArrayList<String>();
		ArrayList<Object> paramValues = new ArrayList<Object>();
		
		StringBuilder query = new StringBuilder("SELECT carespell ");
		query.append(" FROM CatsReferral AS referral LEFT JOIN referral.careContext AS carecontext ");
		query.append(" LEFT JOIN carecontext.episodeOfCare AS episodeofcare LEFT JOIN episodeofcare.careSpell AS carespell ");
		
		query.append(" WHERE ");
		
		query.append(" referral.id = :REFERRAL_ID ");
		
		paramNames.add("REFERRAL_ID");
		paramValues.add(referral.getID_CatsReferral());
		
		return CareSpellForRequestServiceShortVoAssembler.create((CareSpell) getDomainFactory().findFirst(query.toString(), paramNames, paramValues));
	}

	public ServiceLiteVoCollection getReferralService(ContractConfigRefVo contractRef, String serviceName)
	{
		if(contractRef == null || contractRef.getID_ContractConfig() == null)
			return null;
		
		DomainFactory factory = getDomainFactory();

		/* TODO MSSQL case - String query = "select s from ContractConfig as cc left join cc.serviceLocations as sl left join sl.service as s where ( cc.id = :ContractConfig and s.isActive=1 and s.upperName like :ServiceName)  order by s.serviceName asc  "; */
		String query = "select s from ContractConfig as cc left join cc.serviceLocations as sl left join sl.service as s where ( cc.id = :ContractConfig and s.isActive = TRUE and s.upperName like :ServiceName)  order by s.serviceName asc  ";
		
		List doServices = factory.find(query, new String[] {"ContractConfig", "ServiceName"}, new Object[] {contractRef.getID_ContractConfig(), serviceName.toUpperCase() + "%"});

		return ServiceLiteVoAssembler.createServiceLiteVoCollectionFromService(doServices);
	}
}
