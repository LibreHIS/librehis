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
// This code was generated by George Cristian Josan using IMS Development Environment (version 1.80 build 4087.18341)
// Copyright (C) 1995-2011 IMS MAXIMS. All rights reserved.

package ims.RefMan.domain.impl;

import ims.RefMan.domain.base.impl.BaseClinicalOutcomeConfigImpl;
import ims.clinical.configuration.domain.objects.ClinicalOutcomeConfig;
import ims.clinical.configuration.vo.ClinicalOutcomeConfigRefVo;
import ims.clinical.vo.lookups.ClinicalOutcomeCategory;
import ims.clinicaladmin.vo.ClinicalOutcomeConfigDisplayVo;
import ims.clinicaladmin.vo.ClinicalOutcomeConfigDisplayVoCollection;
import ims.clinicaladmin.vo.ClinicalOutcomeConfigVo;
import ims.clinicaladmin.vo.domain.ClinicalOutcomeConfigLiteVoAssembler;
import ims.clinicaladmin.vo.domain.ClinicalOutcomeConfigVoAssembler;
import ims.core.vo.lookups.Specialty;
import ims.core.vo.lookups.TaxonomyType;
import ims.domain.DomainFactory;
import ims.domain.exceptions.DomainRuntimeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClinicalOutcomeConfigImpl extends BaseClinicalOutcomeConfigImpl
{

	private static final long serialVersionUID = 1L;

	/**
	 *  This function will retrieve the Clinical Outcome records matching search criteria and selected record if any
	 */
	public ClinicalOutcomeConfigDisplayVoCollection listClinicalOutcomes(ClinicalOutcomeConfigRefVo selectedRecord, ClinicalOutcomeCategory category, Specialty specialty,
			String clinicalOutcome, String code, Boolean activeOnly)
	{
		// String Builder for query
		StringBuilder query = new StringBuilder();
		query.append("select outcome, taxType.id, map.taxonomyCode from ClinicalOutcomeConfig as outcome left join outcome.codeMappings as map left join map.taxonomyName as taxType");
		
		// ArrayList for parameters
		ArrayList<String> paramNames = new ArrayList<String>();
		ArrayList<Object> paramValues = new ArrayList<Object>();
		
		// String Builder for conditions
		ArrayList<String> conditions = new ArrayList<String>();
		
		// Condition needing OR
		boolean needOR = false;
		
		// Add condition for selected record
		if (selectedRecord != null && selectedRecord.getID_ClinicalOutcomeConfigIsNotNull())
		{
			conditions.add("outcome.id = :ID");
			
			paramNames.add("ID");
			paramValues.add(selectedRecord.getID_ClinicalOutcomeConfig());
			
			// Mark as needing OR
			needOR = true;
		}
		
		// Add condition for category
		if (category != null)
		{
			conditions.add("outcome.category.id = :CAT_ID");
			
			paramNames.add("CAT_ID");
			paramValues.add(category.getID());
		}
		
		// Add condition for speciality
		if (specialty != null)
		{
			conditions.add("outcome.specialty.id = :SPEC_ID");
			
			paramNames.add("SPEC_ID");
			paramValues.add(specialty.getID());
		}
		
		// Add condition for Clinical outcome
		if (clinicalOutcome != null && clinicalOutcome.length() > 0)
		{
			conditions.add("UPPER(outcome.clinicalOutcome) like :OUTCOME");
			
			paramNames.add("OUTCOME");
			paramValues.add("%" + clinicalOutcome.toUpperCase() + "%");
		}
		
		
		// Add conditions for Code
		if (code != null && code.length() > 0)
		{
			conditions.add("UPPER(map.taxonomyCode) like :CODE and taxType.id = :TAX_TYPE");
			
			paramNames.add("CODE");
			paramValues.add("%" + code.toUpperCase() + "%");

			paramNames.add("TAX_TYPE");
			paramValues.add(TaxonomyType.CLINICAL_OUTCOME_CODE.getID());
		}
		
		// Add condition for active only
		if (Boolean.TRUE.equals(activeOnly))
		{
			/* TODO MSSQL case - conditions.add("outcome.isActive = 1"); */
			conditions.add("outcome.isActive = true");
		}
		
		
		// Build query (add conditions if necessary)
		if (conditions != null && conditions.size() > 0)
		{
			query.append(" where ");

			query.append(conditions.get(0));

			boolean closeBracker = false;
			for (int i = 1; i < conditions.size(); i++)
			{
				if (needOR)
				{
					query.append(" or (");
					closeBracker = true;
				}
				else
					query.append(" and ");

				query.append(conditions.get(i));
				
				needOR = false;
			}
			
			if (closeBracker) query.append(")");
		}
		
		query.append(" order by UPPER(outcome.clinicalOutcome) asc");
		
		
		// Query for display records
		@SuppressWarnings("unchecked")
		List<Object[]> results = getDomainFactory().find(query.toString(), paramNames, paramValues);

		// Prepare iterator for results
		Iterator<Object[]> resultsIterator = results.iterator();
		
		// Create results collection to be returned
		ClinicalOutcomeConfigDisplayVoCollection displayResults = new ClinicalOutcomeConfigDisplayVoCollection();

		// Iterate results collection
		while (resultsIterator.hasNext())
		{
			// Get next object value
			Object[] value = resultsIterator.next();
			
			// Create outcome
			if (value.length > 0)
			{
				// Create new display result
				ClinicalOutcomeConfigDisplayVo displayVo = new ClinicalOutcomeConfigDisplayVo(ClinicalOutcomeConfigLiteVoAssembler.create((ClinicalOutcomeConfig) value[0]));
				
				for (int i = 1; i + 1 < value.length && !displayVo.getDisplayTaxonomyIsNotNull(); i += 2)
				{
					if (value[i] instanceof Integer && TaxonomyType.CLINICAL_OUTCOME_CODE.getID() == (Integer)value[i] && value[i + 1] instanceof String)
					{
						displayVo.setDisplayTaxonomy((String) value[i + 1]);
						
						if (displayResults.contains(displayVo))
							displayResults.remove(displayVo);
					}
				}
				
				// Add display result to display results
				displayResults.add(displayVo);
			}
		}

		// Return display results
		return displayResults;
	}

	
	/**
	 * This function will retrieve a specific Clinical Outcome record based on provided ID
	 */
	public ClinicalOutcomeConfigVo getClinicalOutcome(ClinicalOutcomeConfigRefVo clinicalOutcome)
	{
		if (clinicalOutcome == null || !clinicalOutcome.getID_ClinicalOutcomeConfigIsNotNull())
			return null;
		
		return ClinicalOutcomeConfigVoAssembler.create((ClinicalOutcomeConfig) getDomainFactory().getDomainObject(ClinicalOutcomeConfig.class, clinicalOutcome.getID_ClinicalOutcomeConfig()));
	}

	
	/**
	 * This function will attempt to commit to database a Clinical Outcome record
	 */
	public ClinicalOutcomeConfigVo saveClinicalOutcome(ClinicalOutcomeConfigVo clinicalOutcome) throws ims.domain.exceptions.DomainInterfaceException, ims.domain.exceptions.StaleObjectException, ims.domain.exceptions.ForeignKeyViolationException, ims.domain.exceptions.UniqueKeyViolationException
	{
		// Check for valid record
		if (clinicalOutcome == null)
			throw new DomainRuntimeException("Can not save null record");
		
		// Check record for validation
		if (!clinicalOutcome.isValidated())
			throw new DomainRuntimeException("Can not save record not validated");
		
		
		// Get domain factory
		DomainFactory factory = getDomainFactory();

		// Extract ICP domain object
		ClinicalOutcomeConfig domainOutcome = ClinicalOutcomeConfigVoAssembler.extractClinicalOutcomeConfig(factory, clinicalOutcome);

		// Save domain object
		factory.save(domainOutcome);

		// Return saved domain object
		return ClinicalOutcomeConfigVoAssembler.create(domainOutcome);
	}
}
