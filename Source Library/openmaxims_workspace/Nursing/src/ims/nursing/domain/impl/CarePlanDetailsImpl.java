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
// This code was generated by John Pearson using IMS Development Environment (version 1.18 build 40628.1730)
// Copyright (C) 1995-2004 IMS MAXIMS plc. All rights reserved.

package ims.nursing.domain.impl;

import ims.clinical.domain.objects.Discharge;
import ims.coe.vo.domain.DischargeAssembler;
import ims.core.admin.vo.CareContextRefVo;
import ims.core.domain.Reports;
import ims.core.domain.impl.ReportsImpl;
import ims.domain.DomainFactory;
import ims.domain.exceptions.DomainInterfaceException;
import ims.domain.exceptions.DomainRuntimeException;
import ims.domain.exceptions.StaleObjectException;
import ims.domain.impl.DomainImpl;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.utils.Date;
import ims.nursing.careplans.domain.objects.CarePlan;
import ims.nursing.careplans.domain.objects.CarePlanTemplate;
import ims.nursing.careplans.vo.CarePlanRefVo;
import ims.nursing.vo.CarePlanCollection;
import ims.nursing.vo.CarePlanEvaluationNote;
import ims.nursing.vo.CarePlanEvaluationNoteCollection;
import ims.nursing.vo.CarePlanTemplateInterventionCollection;
import ims.nursing.vo.NursingClinicalNotesVoCollection;
import ims.nursing.vo.domain.CarePlanAssembler;
import ims.nursing.vo.domain.CarePlanEvaluationNoteAssembler;
import ims.nursing.vo.domain.CarePlanTemplateInterventionAssembler;
import ims.nursing.vo.domain.NursingClinicalNotesVoAssembler;

import java.util.List;

public class CarePlanDetailsImpl extends DomainImpl implements ims.nursing.domain.CarePlanDetails, ims.nursing.domain.CarePlanReviewDialog, ims.domain.impl.Transactional
{
	/**
	 * saves the interventions and the evaluation note
	 */
	public ims.nursing.vo.CarePlan saveCarePlan(ims.nursing.vo.CarePlan carePlan) throws StaleObjectException
	{
		// Ensure the value object has been validated
		if (!carePlan.isValidated())
			throw new DomainRuntimeException("CarePlan has not been validated");

		DomainFactory factory = getDomainFactory();

		CarePlan cp = CarePlanAssembler.extractCarePlan(factory, carePlan);

		factory.save(cp);
		return (CarePlanAssembler.create(cp));
	}

	public CarePlanTemplateInterventionCollection getCarePlanTemplateActions(ims.nursing.vo.CarePlanTemplate voCarePlanTemplate)
	{
		DomainFactory factory = getDomainFactory();

		CarePlanTemplate templ = (CarePlanTemplate) factory.getDomainObject(CarePlanTemplate.class, voCarePlanTemplate.getID_CarePlanTemplate());
		return (CarePlanTemplateInterventionAssembler.createCarePlanTemplateInterventionCollectionFromCarePlanTemplateIntervention(templ.getInterventions()));
	}

	/**
	 * Retrieves the selected care plan
	 */
	public ims.nursing.vo.CarePlan getCarePlan(ims.nursing.vo.CarePlan voCarePlan)
	{
		DomainFactory factory = getDomainFactory();

		CarePlan cp = (CarePlan) factory.getDomainObject(CarePlan.class, voCarePlan.getID_CarePlan());
		ims.nursing.vo.CarePlan voCp = CarePlanAssembler.create(cp);
		return voCp;
	}

	/**
	 * Retrieves discharge
	 */
	public ims.coe.vo.Discharge getDischarge(CareContextRefVo careContext)
	{
		if (careContext.getID_CareContext() == null)
			throw new CodingRuntimeException("careContext id is null in method listDischarge");

		List discharges = getDomainFactory().find("from Discharge dis where dis.careContext.id = :ccId", new String[]{"ccId"}, new Object[]{careContext.getID_CareContext()});

		if (discharges != null && discharges.size() > 0)
			return DischargeAssembler.create((Discharge) discharges.get(0));
		else
			return null;
	}

	public String[] getReportAndTemplate(Integer nReportId, Integer nTemplateId) throws DomainInterfaceException
	{
		Reports impl = (Reports) getDomainImpl(ReportsImpl.class);
		return impl.getReportAndTemplate(nReportId, nTemplateId);
	}

	public NursingClinicalNotesVoCollection listClinicalNotesForCarePlanAndDateRange(CarePlanRefVo carePlanRefVo, Date dateFrom, Date dateTo, Boolean activeOnly)
	{
		if (carePlanRefVo != null && carePlanRefVo.getID_CarePlan() == null || dateFrom == null || dateTo == null || activeOnly == null)
			throw new CodingRuntimeException("Mandatory arguments not supplied to listClinicalNotesForCarePlanAndDateRange method");

		String hql = "from NursingClinicalNotes ncn join fetch ncn.carePlans as carePlan where carePlan.id = :idCarePlan and (ncn.recordingDateTime >= :startDate and ncn.recordingDateTime < :endDate ) ";
		if(activeOnly == true)
		{
			/* TODO MSSQL case - hql += "and ncn.isCorrected <> 1"; */
			hql += "and ncn.isCorrected <> true";
		}
		
		List notes = getDomainFactory().find(hql, new String[]{"idCarePlan", "startDate", "endDate"},new Object[]{carePlanRefVo.getID_CarePlan(), dateFrom.getDate(), dateTo.copy().addDay(1).getDate()});
		return NursingClinicalNotesVoAssembler.createNursingClinicalNotesVoCollectionFromNursingClinicalNotes(notes);
	}

	public CarePlanEvaluationNoteCollection listEvaluationNotesforCarePlanAndByDateRange(CarePlanRefVo carePlanRefVo, Date dateFrom, Date dateTo, Boolean activeOnly)
	{
		if (carePlanRefVo != null && carePlanRefVo.getID_CarePlan() == null || dateFrom == null || dateTo == null)
			throw new CodingRuntimeException("Mandatory arguments not supplied to listClinicalNotesForCarePlanAndDateRange method");
		
		String hql = "from ims.nursing.careplans.domain.objects.CarePlanEvaluationNote cpen where cpen.carePlan.id = :idCarePlan and (cpen.recordedDateTime >= :startDate and cpen.recordedDateTime < :endDate ) ";
		if(activeOnly == true)
			hql += "and cpen.active = true";
		
		List notes = getDomainFactory().find(hql, new String[]{"idCarePlan", "startDate", "endDate"},new Object[]{carePlanRefVo.getID_CarePlan(), dateFrom.getDate(), dateTo.copy().addDay(1).getDate()});
		return CarePlanEvaluationNoteAssembler.createCarePlanEvaluationNoteCollectionFromCarePlanEvaluationNote(notes);
	}

	public void saveEvaluationNote(CarePlanEvaluationNote voNote) throws StaleObjectException
	{
		if (voNote == null)
			throw new CodingRuntimeException("voNote is null in method saveEvaluationNote");
		if (!voNote.isValidated())
			throw new CodingRuntimeException("voNote has not been validated in method saveEvaluationNote");
		
		ims.nursing.careplans.domain.objects.CarePlanEvaluationNote doNote =  CarePlanEvaluationNoteAssembler.extractCarePlanEvaluationNote(getDomainFactory(), voNote);
		getDomainFactory().save(doNote);
	}

	public CarePlanCollection listCarePlans(ims.nursing.vo.CarePlan voCarePlan)	throws DomainInterfaceException 
	{
		DomainFactory factory = getDomainFactory();

		CarePlan cp = (CarePlan) factory.getDomainObject(CarePlan.class, voCarePlan.getID_CarePlan());
		ims.nursing.vo.CarePlan voCp = CarePlanAssembler.create(cp);
		CarePlanCollection temVoColl = new CarePlanCollection();
		temVoColl.add(voCp);
		return temVoColl;

	}

	
}
