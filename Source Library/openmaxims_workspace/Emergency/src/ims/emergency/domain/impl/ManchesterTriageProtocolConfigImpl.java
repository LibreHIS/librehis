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
// This code was generated by Bogdan Tofei using IMS Development Environment (version 1.80 build 4342.23748)
// Copyright (C) 1995-2012 IMS MAXIMS. All rights reserved.

package ims.emergency.domain.impl;

import ims.clinical.vo.ClinicalProblemShortVo;
import ims.clinical.vo.ClinicalProblemShortVoCollection;
import ims.clinical.vo.domain.ClinicalProblemShortVoAssembler;
import ims.core.vo.TaxonomyMap;
import ims.core.vo.lookups.PreActiveActiveInactiveStatus;
import ims.core.vo.lookups.TaxonomyType;
import ims.domain.DomainFactory;
import ims.domain.exceptions.DomainRuntimeException;
import ims.domain.exceptions.StaleObjectException;
import ims.domain.exceptions.UniqueKeyViolationException;
import ims.emergency.configuration.domain.objects.ManchesterTriageProtocolConfiguration;
import ims.emergency.domain.base.impl.BaseManchesterTriageProtocolConfigImpl;
import ims.emergency.vo.ManchesterTriageProtocolConfigLiteVo;
import ims.emergency.vo.ManchesterTriageProtocolConfigLiteVoCollection;
import ims.emergency.vo.ManchesterTriageProtocolConfigVo;
import ims.emergency.vo.domain.ManchesterTriageProtocolConfigLiteVoAssembler;
import ims.emergency.vo.domain.ManchesterTriageProtocolConfigVoAssembler;
import ims.framework.exceptions.CodingRuntimeException;
import ims.vo.LookupInstVo;

import java.util.ArrayList;
import java.util.List;

public class ManchesterTriageProtocolConfigImpl extends BaseManchesterTriageProtocolConfigImpl
{
	private static final long serialVersionUID = 1L;

	public ClinicalProblemShortVoCollection listProblems(String problemName)
	{
		DomainFactory factory = getDomainFactory();

		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();

		if (problemName == null || (problemName != null && problemName.length() == 0))
			throw new DomainRuntimeException("String for search is null.");

		String probNameLite = problemName != null ? problemName.toUpperCase() + "%" : "%%";

		StringBuffer hql = new StringBuffer();

		/* TODO MSSQL case - hql.append(" select probl from ClinicalProblem as probl left join probl.keywords as k where (probl.pCName like :ClinicalProblemSearchText or k.keyword like :ClinicalProblemSearchText) and probl.isActive = 1"); */
		hql.append(" select probl from ClinicalProblem as probl left join probl.keywords as k where (probl.pCName like :ClinicalProblemSearchText or k.keyword like :ClinicalProblemSearchText) and probl.isActive = true");

		hql.append(" order by UPPER(probl.pCName) asc");

		markers.add("ClinicalProblemSearchText");
		values.add(probNameLite);

		return ClinicalProblemShortVoAssembler.createClinicalProblemShortVoCollectionFromClinicalProblem(factory.find(hql.toString(), markers, values));
	}

	public ManchesterTriageProtocolConfigVo saveTriage(ManchesterTriageProtocolConfigVo triageProtocol) throws StaleObjectException, UniqueKeyViolationException
	{
		if (triageProtocol == null)
			throw new CodingRuntimeException("Cannot save null TriageProtocol");

		if (!triageProtocol.isValidated())
			throw new DomainRuntimeException("triageProtocolVo Not Validated.");

		DomainFactory factory = getDomainFactory();

		triageProtocol.setProtocolName(triageProtocol.getProtocolName().trim());

		ManchesterTriageProtocolConfigVo savedProtocol = null;

		ManchesterTriageProtocolConfiguration domProtocol = ManchesterTriageProtocolConfigVoAssembler.extractManchesterTriageProtocolConfiguration(factory, triageProtocol);

		if (triageProtocol.getTaxonomyMapIsNotNull())
		{
			boolean found = false;

			StringBuilder errors = new StringBuilder();

			for (int i = 0; i < triageProtocol.getTaxonomyMap().size(); i++)
			{
				TaxonomyMap code = triageProtocol.getTaxonomyMap().get(i);
				ManchesterTriageProtocolConfiguration triageRole = getDiscriminatorRoleByTaxonomy(code.getTaxonomyCode(), code.getTaxonomyName());
				
				if (triageRole != null && !triageRole.getId().equals(triageProtocol.getID_ManchesterTriageProtocolConfiguration()))
				{
					found = true;

					errors.append("\nProtocol '" + triageRole.getProtocolName() + "' already has mapped :");
					errors.append(" External Code Type '" + code.getTaxonomyName() + "' and code '" + code.getTaxonomyCode() + "'");
				}
			}

			if (found)
				throw new UniqueKeyViolationException(errors.toString());
		}

		factory.save(domProtocol);

		savedProtocol = ManchesterTriageProtocolConfigVoAssembler.create(domProtocol);

		return savedProtocol;
	}

	private ManchesterTriageProtocolConfiguration getDiscriminatorRoleByTaxonomy(String taxonomyCode, TaxonomyType taxonomyName)
	{
		if (taxonomyCode == null || taxonomyName == null)
			return null;

		DomainFactory factory = (DomainFactory) this.getDomainFactory();
		String hql = " from ManchesterTriageProtocolConfiguration tp join tp.taxonomyMap as tm where tm.taxonomyName = :taxType and tm.taxonomyCode = :extId ";
		List<?> discriminator = factory.find(hql, new String[] { "taxType", "extId" }, new Object[] { getDomLookup(taxonomyName), taxonomyCode });

		if (discriminator != null && discriminator.size() >= 1)
			return (ManchesterTriageProtocolConfiguration) discriminator.get(0);

		return null;
	}

	public ManchesterTriageProtocolConfigLiteVoCollection listTriageProtocols(String triageName, ClinicalProblemShortVo clinicalProblem, LookupInstVo triageStatus)
	{
		DomainFactory factory = getDomainFactory();

		String hql = " select triage from ManchesterTriageProtocolConfiguration as triage ";
		StringBuffer condStr = new StringBuffer();
		String andStr = " ";

		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();

		if (triageName != null)
		{
			triageName.trim();
			triageName = triageName.toUpperCase();
			triageName = "%" + triageName + "%";
			condStr.append(andStr + " where upper(triage.protocolName) like :protocolName");
			markers.add("protocolName");
			values.add(triageName);
			andStr = " and ";
		}
		else
			andStr = " where ";

		if (clinicalProblem != null)
		{
			condStr.append(andStr + " triage.problem.id = :pName");
			markers.add("pName");
			values.add(clinicalProblem.getID_ClinicalProblem());
			andStr = " and ";
		}

		if (triageStatus != null)
		{
			condStr.append(andStr + " triage.activeStatus.id = :tStatus");
			markers.add("tStatus");
			values.add(triageStatus.getID());
		}

		hql += condStr.toString();
		return ManchesterTriageProtocolConfigLiteVoAssembler.createManchesterTriageProtocolConfigLiteVoCollectionFromManchesterTriageProtocolConfiguration(factory.find(hql, markers, values)).sort();
	}

	public ManchesterTriageProtocolConfigVo getTriageProtocol(ManchesterTriageProtocolConfigLiteVo selectedTriageProtocol)
	{
		if (selectedTriageProtocol == null || selectedTriageProtocol.getID_ManchesterTriageProtocolConfiguration() == null)
		{
			throw new CodingRuntimeException("Cannot get ManchesterTriageProtocolConfigVo on null Id ");
		}

		DomainFactory factory = getDomainFactory();

		StringBuffer hql = new StringBuffer();

		hql.append(" select protocol from ManchesterTriageProtocolConfiguration as protocol where protocol.id = :protocolID");

		List<?> list = factory.find(hql.toString(), new String[] { "protocolID" }, new Object[] { selectedTriageProtocol.getID_ManchesterTriageProtocolConfiguration() });

		if (list != null && list.size() > 0)
			return ManchesterTriageProtocolConfigVoAssembler.createManchesterTriageProtocolConfigVoCollectionFromManchesterTriageProtocolConfiguration(list).get(0);

		return null;
	}

	public ManchesterTriageProtocolConfigLiteVoCollection checkAlreadyActive(ManchesterTriageProtocolConfigLiteVo triageProtocol)
	{
		DomainFactory factory = getDomainFactory();

		ArrayList<String> markers = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();

		if (triageProtocol == null)
		{
			throw new CodingRuntimeException("Cannot get ManchesterTriageProtocolConfigVo on null Id ");
		}

		StringBuffer hql = new StringBuffer();

		hql.append(" select triageProtocol from ManchesterTriageProtocolConfiguration as triageProtocol left join triageProtocol.problem as problem left join triageProtocol.activeStatus as active where problem.id = :problemID and active.id = :idActive ");

		markers.add("problemID");
		values.add(triageProtocol.getProblem().getID_ClinicalProblem());
		markers.add("idActive");
		values.add(PreActiveActiveInactiveStatus.ACTIVE.getID());

		return ManchesterTriageProtocolConfigLiteVoAssembler.createManchesterTriageProtocolConfigLiteVoCollectionFromManchesterTriageProtocolConfiguration(factory.find(hql.toString(), markers, values));

	}
}
