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
// This code was generated by Cristian Belciug using IMS Development Environment (version 1.80 build 5443.18271)
// Copyright (C) 1995-2014 IMS MAXIMS. All rights reserved.

package ims.RefMan.domain.impl;

import ims.RefMan.domain.base.impl.BaseUpdateUrgencyImpl;
import ims.RefMan.domain.objects.CatsReferral;
import ims.RefMan.vo.CatsReferralForChangeUrgencyVo;
import ims.RefMan.vo.domain.CatsReferralForChangeUrgencyVoAssembler;
import ims.RefMan.vo.lookups.ReferralUrgency;
import ims.core.vo.ReferralSourceUrgencyVoCollection;
import ims.core.vo.domain.ReferralSourceUrgencyVoAssembler;
import ims.core.vo.lookups.TaxonomyType;
import ims.domain.DomainFactory;
import ims.domain.exceptions.DomainInterfaceException;
import ims.domain.exceptions.StaleObjectException;
import ims.framework.enumerations.SystemLogLevel;
import ims.framework.enumerations.SystemLogType;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.utils.Date;
import ims.framework.utils.DateTime;
import ims.pathways.configuration.vo.TargetRefVo;
import ims.pathways.domain.HL7PathwayIf;
import ims.pathways.domain.impl.HL7PathwayIfImpl;
import ims.pathways.domain.objects.PathwayClock;
import ims.pathways.domain.objects.PatientPathwayJourney;
import ims.pathways.vo.PatientEventVo;
import ims.pathways.vo.PatientPathwayJourneyRefVo;
import ims.pathways.vo.domain.PatientJourneyVoAssembler;
import ims.pathways.vo.lookups.EventStatus;

import java.util.List;

public class UpdateUrgencyImpl extends BaseUpdateUrgencyImpl
{

	private static final long serialVersionUID = 1L;

	public ims.RefMan.vo.CatsReferralForChangeUrgencyVo getCatsReferral(ims.RefMan.vo.CatsReferralRefVo catsReferral)
	{
		if(catsReferral == null)
			return null;
		
		return CatsReferralForChangeUrgencyVoAssembler.create((CatsReferral) getDomainFactory().getDomainObject(CatsReferral.class, catsReferral.getID_CatsReferral()));
	}

	public Boolean saveCatsReferral(CatsReferralForChangeUrgencyVo catsReferral, Boolean isDowngradeUrgency, Boolean isConsultantUpgrade, Date consultantUpgradeDate, Boolean removeRTTData) throws ims.domain.exceptions.StaleObjectException
	{
		if(catsReferral == null)
			throw new CodingRuntimeException("Cannot save a null CatsReferralForChangeUrgencyVo.");
		
		if(!catsReferral.isValidated())
			throw new CodingRuntimeException("CatsReferralForChangeUrgencyVo is not validated.");
		
		DomainFactory factory = getDomainFactory();
		HL7PathwayIf hl7Impl = (HL7PathwayIf) getDomainImpl(HL7PathwayIfImpl.class);
		
		CatsReferral doCatsReferral = CatsReferralForChangeUrgencyVoAssembler.extractCatsReferral(factory, catsReferral);
		
		PatientPathwayJourney domJourney = null;
		PatientPathwayJourneyRefVo journeyRefVo = null;
		
		if(doCatsReferral.getJourney() != null)
		{
			domJourney = (PatientPathwayJourney)factory.getDomainObject(PatientPathwayJourney.class, doCatsReferral.getJourney().getId());
			journeyRefVo = new PatientPathwayJourneyRefVo(domJourney.getId(), domJourney.getVersion());
		}
		
		if(Boolean.TRUE.equals(isDowngradeUrgency))
		{
			if(domJourney != null)
			{
    			try
    			{
    				TargetRefVo removeTarget = hl7Impl.getTargetByTaxonomyMap(TaxonomyType.PAS, "62D");
        			if(removeTarget != null)
        			{
        				hl7Impl.takeTargetOutOfScopeWithoutEvent(removeTarget, journeyRefVo); 
        			}
        			
        			removeTarget = hl7Impl.getTargetByTaxonomyMap(TaxonomyType.PAS, "31D");
        			if (removeTarget != null)
        			{
        				hl7Impl.takeTargetOutOfScopeWithoutEvent(removeTarget, journeyRefVo);
        			}
        			
        			// WDEV-20636 Remove Decision to Treat urgency too if there
        			removeTarget = hl7Impl.getTargetByTaxonomyMap(TaxonomyType.PAS, "DTT31");
        			if (removeTarget != null)
        			{
        				hl7Impl.takeTargetOutOfScopeWithoutEvent(removeTarget, journeyRefVo);
        			}
    			}
    			catch (DomainInterfaceException e) 
    			{
    				super.createSystemLogEntry(SystemLogType.APPLICATION, SystemLogLevel.WARNING, e.getMessage());
    			}
    			
    			// WDEV-20636 Downgrade so unset the isCancerPathway indicator on the PatientJourney
    			domJourney.setIsCancerPathway(false);
    			domJourney.setCancerPathwayDate(null);
    			
    			try 
    			{
    				ims.pathways.vo.EventVo pthwEvent = hl7Impl.getEventByTaxonomyMap(TaxonomyType.PAS, "DGD");
    				if (pthwEvent != null)
    				{
    					PatientEventVo patientEvent = new PatientEventVo();
    					patientEvent.setEventDateTime(new DateTime());
    					patientEvent.setEvent(pthwEvent);
    					patientEvent.setJourney(PatientJourneyVoAssembler.create(domJourney));
    					patientEvent.setPatient(catsReferral.getPatient());
    					patientEvent.setEventStatus(EventStatus.ACTIVE);
    					
    					hl7Impl.instantiatePatientEvent(patientEvent, catsReferral);  // WDEV-23784
    				}
    			}
    			catch (DomainInterfaceException e) 
    			{
    				super.createSystemLogEntry(SystemLogType.APPLICATION, SystemLogLevel.WARNING, "Event was not found for Downgrade from TwoWeekWait or National Screening or Consultant Upgrade - Pas mapping = DGD " + e.getMessage()); //WDEV-19700
    			}
			}
			
			doCatsReferral.setCurrent31TargetDate(null);
			doCatsReferral.setCurrent62TargetDate(null);
		}
		if (ReferralUrgency.EMERGENCY.equals(catsReferral.getUrgency()) && Boolean.TRUE.equals(removeRTTData)) //WDEV-21176
		{	
			doCatsReferral = updateReferralToEmergency(factory,doCatsReferral,domJourney); 
		}
		factory.save(doCatsReferral);
		
		if(Boolean.TRUE.equals(isConsultantUpgrade))
		{
			if (domJourney != null)
			{
				domJourney.setIsCancerPathway(true);
				domJourney.setCancerPathwayDate(consultantUpgradeDate.getDate());
				factory.save(domJourney);
			
    			try 
    			{
    				ims.pathways.vo.EventVo pthwEvent = hl7Impl.getEventByTaxonomyMap(TaxonomyType.PAS, "CONS");
    				if (pthwEvent != null)
    				{
    					PatientEventVo patientEvent = new PatientEventVo();
    					patientEvent.setEvent(pthwEvent);
    					patientEvent.setEventDateTime(new DateTime(consultantUpgradeDate));
    					
    					patientEvent.setJourney(PatientJourneyVoAssembler.create(domJourney));
    					patientEvent.setPatient(catsReferral.getPatient());
    					patientEvent.setEventStatus(EventStatus.ACTIVE);
    
    					hl7Impl.instantiatePatientEvent(patientEvent, catsReferral); // WDEV-23784
    				}
    			}
    			catch (DomainInterfaceException e) 
    			{
    				super.createSystemLogEntry(SystemLogType.APPLICATION, SystemLogLevel.WARNING, "Event was not found for Consultant Upgrade - Pas mapping = CONS");
    			}
    			
    			if(Boolean.TRUE.equals(doCatsReferral.isRTTClockImpact()))
    			{
        			try 
        			{
        				TargetRefVo target = hl7Impl.getTargetByTaxonomyMap(TaxonomyType.PAS, "62D");
        				hl7Impl.bringTargetIntoScopeWithoutEvent(target, journeyRefVo, consultantUpgradeDate);
        			}
        			catch (DomainInterfaceException e) 
        			{
        				super.createSystemLogEntry(SystemLogType.APPLICATION, SystemLogLevel.WARNING, "saveConsultantUpgrade - " + e.getMessage());
        			}
    			}
			}
		}
		
		return true;
	}
	//WDEV-21176
	private CatsReferral updateReferralToEmergency(DomainFactory factory, CatsReferral doCatsReferral, PatientPathwayJourney domJourney) throws StaleObjectException
	{
		if (doCatsReferral == null || doCatsReferral.getId() == null)
			return null;
		
		doCatsReferral.setIsEmergencyReferral(true);
		doCatsReferral.setRTTClockImpact(false);
		if (doCatsReferral.getReferralDetails() != null)
		{	
			doCatsReferral.getReferralDetails().setEnd18WW(null);
			doCatsReferral.getReferralDetails().setEndDateKPI(null);
			doCatsReferral.getReferralDetails().setEndDatePaperKPI(null);
			doCatsReferral.getReferralDetails().setEndDateEmailKPI(null);
		}
		if (domJourney != null)
		{
			doCatsReferral.setRTTClockImpact(false);
			
			PathwayClock clockDO = domJourney.getCurrentClock();
			if (clockDO != null)
				factory.markAsRie(PathwayClock.class, clockDO.getId(), null, doCatsReferral.getPatient().getId(), null, null, "RTT information removed as a result of referral urgency change to Emergency");
			
			factory.markAsRie(PatientPathwayJourney.class, domJourney.getId(), null, doCatsReferral.getPatient().getId(), null, null, "RTT information removed as a result of referral urgency change to Emergency");
			doCatsReferral.setJourney(null);
			doCatsReferral.setCurrentRTTStatus(null);
			doCatsReferral.setCurrent31TargetDate(null);
			doCatsReferral.setCurrent62TargetDate(null);			
		}

		return doCatsReferral;
	}
	
	//WDEV-21034
	public ReferralSourceUrgencyVoCollection getSourceOfReferralConfigVoColl()
	{
		DomainFactory factory = getDomainFactory();
		StringBuffer hql = new StringBuffer("from ReferralSourceUrgency");
		List<?> list = factory.find(hql.toString());
		if( list != null && list.size() > 0 )
			return ReferralSourceUrgencyVoAssembler.createReferralSourceUrgencyVoCollectionFromReferralSourceUrgency(list);

		return null;
	}

}
