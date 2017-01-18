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
// This code was generated by Catalin Tomozei using IMS Development Environment (version 1.51 build 2490.30195)
// Copyright (C) 1995-2006 IMS MAXIMS plc. All rights reserved.

package ims.admin.forms.practiceadmin;

import ims.admin.forms.practiceadmin.GenForm.ctnDetailsContainer.grdCommChannelsRow;
import ims.admin.forms.practiceadmin.GenForm.ctnDetailsContainer.grdDetailsMappingsRow;
import ims.admin.vo.enums.PracticeSearch;
import ims.configuration.gen.ConfigFlag;
import ims.core.vo.CommChannelVo;
import ims.core.vo.CommChannelVoCollection;
import ims.core.vo.LocSiteVo;
import ims.core.vo.LocSiteVoCollection;
import ims.core.vo.OrganisationVo;
import ims.core.vo.OrganisationWithSitesVo;
import ims.core.vo.PersonAddress;
import ims.core.vo.TaxonomyMap;
import ims.core.vo.TaxonomyMapCollection;
import ims.core.vo.lookups.AddressType;
import ims.core.vo.lookups.LocationType;
import ims.core.vo.lookups.OrganisationType;
import ims.domain.exceptions.ForeignKeyViolationException;
import ims.domain.exceptions.StaleObjectException;
import ims.domain.exceptions.UniqueKeyViolationException;
import ims.framework.enumerations.DialogResult;
import ims.framework.enumerations.FormMode;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.PresentationLogicException;

public class Logic extends BaseLogic
{
	private static final String	NEW_PRACTICE		= "New Practice";
	private static final String	NEW_SURGERY			= "New Surgery";
	private static final String	PRACTICE_DETAILS	= "Practice Details";
	private static final String	SURGERY_DETAILS		= "Surgery Details";
	private static final long serialVersionUID = 1L;
	
	protected void onFormOpen(Object[] args) throws PresentationLogicException
	{
		initialize();
		if (isPracticeAdminDialog())
		{
			OrganisationWithSitesVo voOrgWithSites = form.getGlobalContext().Admin.getPractice();
			if(voOrgWithSites != null)
			{
				if(voOrgWithSites.getID_OrganisationIsNotNull())
				{
					form.customControlPracticeAdmin().addPractice(voOrgWithSites);
					form.customControlPracticeAdmin().setValue(voOrgWithSites);
					editPractice();
				}
				else
				{
					form.ctnDetails().txtDetailsName().setValue(voOrgWithSites.getNameIsNotNull() ? voOrgWithSites.getName() : "");
					populateAddress(voOrgWithSites.getAddressIsNotNull() ? voOrgWithSites.getAddress() : null);
					if (voOrgWithSites.getCodeMappingsIsNotNull())
						if (voOrgWithSites.getCodeMappings().size() > 0)
							populateOrgMappingsGrid(voOrgWithSites.getCodeMappings());
					form.getGlobalContext().Admin.setPractice(null);  //this was only set to hold search criteria. now needs to be cleared.
					newInstance();
					newPractice();
				}
			}
		}
		else
		{
			open();		 
		}
	}

	private void initialize()
	{
		if (ConfigFlag.UI.DEMOGRAPHICS_TYPE.getValue().equals("UK"))
		{
			form.ctnDetails().lblDetailsPostCode().setVisible(true);
			form.ctnDetails().txtDetailsPostCode().setVisible(true);
			form.ctnDetails().lblDetailsCounty().setVisible(false);
			form.ctnDetails().cmbCounty().setVisible(false);
		}
		else if (ConfigFlag.UI.DEMOGRAPHICS_TYPE.getValue().equals("IRISH"))
		{
			form.ctnDetails().lblDetailsPostCode().setVisible(false);
			form.ctnDetails().txtDetailsPostCode().setVisible(false);
			form.ctnDetails().lblDetailsCounty().setVisible(true);
			form.ctnDetails().cmbCounty().setVisible(true);
		}
		
		form.ctnDetails().setCollapsed(true);
		
		if (isPracticeAdminDialog())
			form.btnClose().setVisible(true);
		else
			form.btnClose().setVisible(false);
		
		//Custom Control
		form.customControlPracticeAdmin().setSurgerySelectable(canUseSurgery() ? Boolean.TRUE : Boolean.FALSE);
		form.customControlPracticeAdmin().setVisibleContextMenus(Boolean.TRUE);
		form.customControlPracticeAdmin().setSearchButtonAsDefault();
		form.customControlPracticeAdmin().setVisibleRemoveContextMenu(isRemoveVisible());
	}
	
	private void open()
	{
		//WDEV-11785 removed validate
		if(form.getLocalContext().getPracticeIsNotNull())
		{		
			//form.customControlPracticeAdmin().search(); //WDEV-15663
			//form.customControlPracticeAdmin().addPractice(form.getLocalContext().getPractice());
			form.getLocalContext().setPractice(domain.getPractice(form.getLocalContext().getPractice()));
			if(form.customControlPracticeAdmin().getSelectedSurgery() != null)
			{
				//WDEV-15663
				form.customControlPracticeAdmin().clear();
				form.customControlPracticeAdmin().setValueInGrid(form.getLocalContext().getPractice());
				form.customControlPracticeAdmin().setValue(form.getLocalContext().getSurgery());
				form.getLocalContext().setSurgery(form.customControlPracticeAdmin().getSelectedSurgery());
			}
			else
			{	//WDEV-15663
				form.customControlPracticeAdmin().clear();
				form.customControlPracticeAdmin().setValueInGrid(form.getLocalContext().getPractice());
				//form.customControlPracticeAdmin().setValue(form.getLocalContext().getPractice());
			}
		
		}
		else
		{
			clearInstanceControls();
			//WDEV-15441
			form.ctnDetails().setCollapsed(true);
			setDetailsContainerName(PRACTICE_DETAILS);
			
		}
		
		form.setMode(FormMode.VIEW);
	}

	private void updateControlState()
	{
		if(form.getMode().equals(FormMode.VIEW))
		{
			form.ctnDetails().txtDetailsComment().setEnabled(false);
			form.btnClose().setEnabled(true);
			form.getContextMenus().getGenericGridAddItem().setVisible(false);
			form.btnUpdate().setVisible(form.customControlPracticeAdmin().getSelectedPractice() != null || form.customControlPracticeAdmin().getSelectedSurgery() != null);
			form.ctnDetails().txtDetailsPostCode().setEnabled(false);
			form.ctnDetails().cmbCounty().setEnabled(false);
			form.setcustomControlPracticeAdminEnabled(true);
			form.customControlPracticeAdmin().setVisibleContextMenus(Boolean.TRUE);
		}
		else
		{
			form.ctnDetails().txtDetailsComment().setEnabled(true);
			form.getContextMenus().getGenericGridAddItem().setVisible(true);
			
			form.ctnDetails().txtDetailsPostCode().setEnabled(true);
			form.ctnDetails().cmbCounty().setEnabled(true);
			form.btnClose().setEnabled(false);
			form.setcustomControlPracticeAdminEnabled(false);
			form.customControlPracticeAdmin().setVisibleContextMenus(Boolean.FALSE);
			if (form.ctnDetails().grdDetailsMappings().getSelectedRow()!=null)
			{
				form.getContextMenus().getGenericGridRemoveItem().setVisible(true);
				form.getContextMenus().getGenericGridRemoveItem().setEnabled(true);
			}
			else
			{
				form.getContextMenus().getGenericGridRemoveItem().setVisible(false);
				form.getContextMenus().getGenericGridRemoveItem().setEnabled(false);
			}
		}
		
		form.ctnDetails().lblPCT().setVisible(!(form.getLocalContext().getMenuOptionIsNotNull() && (form.getLocalContext().getMenuOption() == PracticeSearch.NEWSURGERY || form.getLocalContext().getMenuOption() == PracticeSearch.EDITSURGERY || (form.getLocalContext().getMenuOption()== PracticeSearch.SELECT && form.customControlPracticeAdmin().getSelectedSurgery() != null))));
		form.ctnDetails().txtPCT().setVisible(!(form.getLocalContext().getMenuOptionIsNotNull() && (form.getLocalContext().getMenuOption() == PracticeSearch.NEWSURGERY || form.getLocalContext().getMenuOption() == PracticeSearch.EDITSURGERY || (form.getLocalContext().getMenuOption()== PracticeSearch.SELECT && form.customControlPracticeAdmin().getSelectedSurgery() != null))));
		form.ctnDetails().txtPCT().setEnabled(form.getMode().equals(FormMode.EDIT) && !(form.getLocalContext().getMenuOptionIsNotNull() && (form.getLocalContext().getMenuOption() == PracticeSearch.NEWSURGERY || form.getLocalContext().getMenuOption() == PracticeSearch.EDITSURGERY || (form.getLocalContext().getMenuOption()== PracticeSearch.SELECT && form.customControlPracticeAdmin().getSelectedSurgery() != null))));
		
		form.getContextMenus().Admin.getPracticeCommChannelsADDItem().setVisible(form.getMode().equals(FormMode.EDIT));
		form.getContextMenus().Admin.getPracticeCommChannelsREMOVEItem().setVisible(form.getMode().equals(FormMode.EDIT) && form.ctnDetails().grdCommChannels().getValue() instanceof CommChannelVo);
		
		//wdev--9854
		form.ctnDetails().lblDetailsComment().setVisible(!(form.getLocalContext().getMenuOptionIsNotNull() && (form.getLocalContext().getMenuOption() == PracticeSearch.NEWSURGERY || form.getLocalContext().getMenuOption() == PracticeSearch.EDITSURGERY || (form.getLocalContext().getMenuOption()== PracticeSearch.SELECT && form.customControlPracticeAdmin().getSelectedSurgery() != null))));
		form.ctnDetails().txtDetailsComment().setVisible(!(form.getLocalContext().getMenuOptionIsNotNull() && (form.getLocalContext().getMenuOption() == PracticeSearch.NEWSURGERY || form.getLocalContext().getMenuOption() == PracticeSearch.EDITSURGERY || (form.getLocalContext().getMenuOption()== PracticeSearch.SELECT && form.customControlPracticeAdmin().getSelectedSurgery() != null))));
		
		form.ctnDetails().txtDetailsAddress5().setEnabled(!isPds() && form.getMode().equals(FormMode.EDIT));
		
	
	}

	private boolean isPds()
	{
		return !"None".equals(ConfigFlag.DOM.USE_PDS.getValue());
	}
	
	private void clear() 
	{
		form.customControlPracticeAdmin().clear();
		clearInstanceControls();
	}
	
	private void clearInstanceControls()
	{		
		form.ctnDetails().txtDetailsName().setValue(null);
		form.ctnDetails().txtDetailsAddress1().setValue(null);
		form.ctnDetails().txtDetailsAddress2().setValue(null);
		form.ctnDetails().txtDetailsAddress3().setValue(null);
		form.ctnDetails().txtDetailsAddress4().setValue(null);
		form.ctnDetails().txtDetailsAddress5().setValue(null);
		form.ctnDetails().txtDetailsPostCode().setValue(null);
		form.ctnDetails().cmbCounty().setValue(null);
		form.ctnDetails().chkDetailsActive().setValue(false);
		form.ctnDetails().grdCommChannels().getRows().clear();
		form.ctnDetails().grdCommChannels().setValue(null);
		form.ctnDetails().txtPCT().setValue(null);
		form.ctnDetails().txtDetailsComment().setValue(null);
		form.ctnDetails().grdDetailsMappings().getRows().clear();
		form.ctnDetails().grdDetailsMappings().setValue(null);
	}
	
	protected void onGrdResultsListSelectionChanged()
	{
		if (form.customControlPracticeAdmin().getSelectedPractice() != null)
		{
			// the selection is a practice
			//TODO - optimise here to optimise that.... eventually put in the context only OrganisationWithSites, and do a get when saving....
			OrganisationVo organisation = domain.getOrg(form.customControlPracticeAdmin().getSelectedPractice());
			setPracticeToContext(organisation);
			setDetailsContainerName(PRACTICE_DETAILS);
			populateInstanceControls(organisation);
		}
		else if(form.customControlPracticeAdmin().getSelectedSurgery() != null)
		{
			// the selection is a surgery
			LocSiteVo voLocSite = form.customControlPracticeAdmin().getSelectedSurgery();
			OrganisationVo organisation = null;
			if(form.customControlPracticeAdmin().getSelectedPractice() != null)
				organisation = domain.getOrg(form.customControlPracticeAdmin().getSelectedPractice());
			//WDEV-11785 
			else if(form.customControlPracticeAdmin().getParentPractice() != null)				
				organisation = domain.getOrg(form.customControlPracticeAdmin().getParentPractice());
			
			setPracticeToContext(organisation);
			form.getLocalContext().setSurgery(voLocSite);
			
			setDetailsContainerName(SURGERY_DETAILS);
			populateInstanceControls(voLocSite);
		}
		
		form.ctnDetails().setCollapsed(false);
		updateControlState();		
	}
	
	protected void populateInstanceControls(Object record)
	{
			clearInstanceControls();
		if(record == null)
				return;
		OrganisationVo orgRecord = null;
		LocSiteVo locRecord = null;
		PersonAddress voAddress = null;
		CommChannelVoCollection voCommChannels = null;
		
		if (record instanceof OrganisationVo)
		{
			orgRecord = (OrganisationVo)record;
			
			if (orgRecord.getNameIsNotNull())
				form.ctnDetails().txtDetailsName().setValue(orgRecord.getName());
			
			voAddress = orgRecord.getAddress();
			voCommChannels = orgRecord.getCommChannels();
			
			form.ctnDetails().chkDetailsActive().setValue(orgRecord.getIsActive().booleanValue());
			if (orgRecord.getCodeMappingsIsNotNull())
				if (orgRecord.getCodeMappings().size() > 0)
					populateOrgMappingsGrid(orgRecord.getCodeMappings());
			if (orgRecord.getCommentIsNotNull())
				form.ctnDetails().txtDetailsComment().setValue(orgRecord.getComment());	
			
			//wdev-6002
			form.ctnDetails().txtPCT().setValue(orgRecord.getPctCodeIsNotNull() ? orgRecord.getPctCode() : null);
		}
		
		if (record instanceof LocSiteVo)
		{
			locRecord = (LocSiteVo)record;
			
			if (locRecord.getNameIsNotNull())
				form.ctnDetails().txtDetailsName().setValue(locRecord.getName());
			
			voAddress = locRecord.getAddress();
			voCommChannels = locRecord.getCommChannels();
			
			form.ctnDetails().chkDetailsActive().setValue(locRecord.getIsActive().booleanValue());
			if (locRecord.getCodeMappingsIsNotNull())
				if (locRecord.getCodeMappings().size() > 0)
					populateOrgMappingsGrid(locRecord.getCodeMappings());
		}	
		
		populateScreenFromCommChannels(voCommChannels);
		populateAddress(voAddress);
	}
	
	private void populateScreenFromCommChannels(CommChannelVoCollection voCommChannels) 
	{
		if(voCommChannels == null)
			return;
		
		for (int i = 0; i < voCommChannels.size(); i++)
		{
			CommChannelVo voCommChannel = voCommChannels.get(i);

			if (voCommChannel != null && voCommChannel.getChannelTypeIsNotNull())
			{
				grdCommChannelsRow rowCommChannel = form.ctnDetails().grdCommChannels().getRows().newRow();
				rowCommChannel.setcolType(voCommChannel.getChannelType());
				rowCommChannel.setcolContactValue(voCommChannel.getCommValue());
				rowCommChannel.setValue(voCommChannel);
			}
		}
	}

	private void populateAddress(PersonAddress voAddress) 
	{
		if (voAddress != null)
		{
			if (voAddress.getLine1IsNotNull())
				form.ctnDetails().txtDetailsAddress1().setValue(voAddress.getLine1());
			if (voAddress.getLine2IsNotNull())
				form.ctnDetails().txtDetailsAddress2().setValue(voAddress.getLine2());
			if (voAddress.getLine3IsNotNull())
				form.ctnDetails().txtDetailsAddress3().setValue(voAddress.getLine3());
			if (voAddress.getLine4IsNotNull())
				form.ctnDetails().txtDetailsAddress4().setValue(voAddress.getLine4());
			if (voAddress.getLine5IsNotNull())
				form.ctnDetails().txtDetailsAddress5().setValue(voAddress.getLine5());
	
			form.ctnDetails().txtDetailsPostCode().setValue(voAddress.getPostCode());
			form.ctnDetails().cmbCounty().setValue(voAddress.getCounty());
		}
	}

	private void populateOrgMappingsGrid(TaxonomyMapCollection coll)
	{
		form.ctnDetails().grdDetailsMappings().getRows().clear();
		if (coll == null) return;
		
		for (int i = 0; i < coll.size(); i++)
		{
			TaxonomyMap map = coll.get(i);
			grdDetailsMappingsRow row = form.ctnDetails().grdDetailsMappings().getRows().newRow();
			row.setcolExtCodeType(map.getTaxonomyName());
			row.setcolCode(map.getTaxonomyCode());
			row.setTooltipForcolCode(map.getTaxonomyCode());
			row.setValue(map);
		}		
	}
	
	protected void onFormModeChanged()
	{
		updateControlState();
	}
	
	private OrganisationVo populatePracticeData(OrganisationVo value)
	{			
		value.setAddress(populateAddressData(AddressType.PRACTICE));  // WDEV-15936 - specify address type 
		value.setName(form.ctnDetails().txtDetailsName().getValue());
		value.setIsActive(new Boolean(form.ctnDetails().chkDetailsActive().getValue()));
		value.setCodeMappings(getCodeMappings());
		value.setComment(form.ctnDetails().txtDetailsComment().getValue());
		value.setType(OrganisationType.GPP);
		value.setCommChannels(populateCommChannelsFromScreen());
		value.setPctCode(form.ctnDetails().txtPCT().getValue());
		value.setParent(null);
		
		return value;
	}

	private CommChannelVoCollection populateCommChannelsFromScreen() 
	{
		CommChannelVoCollection voCommChannelColl = new CommChannelVoCollection();
		
		for (int i = 0; i < form.ctnDetails().grdCommChannels().getRows().size(); i++)
		{
			grdCommChannelsRow rowComm = form.ctnDetails().grdCommChannels().getRows().get(i);
			
			if (rowComm != null && rowComm.getcolType() != null && (rowComm.getcolContactValue() != null && rowComm.getcolContactValue().trim().length() != 0))
			{
				boolean existCommChannels = false;
				
				for (int k = 0; k < voCommChannelColl.size(); k++)
				{
					if (voCommChannelColl.get(k) != null && voCommChannelColl.get(k).getChannelType() != null && voCommChannelColl.get(k).getChannelType().equals(rowComm.getcolType()) && voCommChannelColl.get(k).getCommValue() != null && voCommChannelColl.get(k).getCommValue().equals(rowComm.getcolContactValue()))
					{
						existCommChannels = true;
						break;
					}
				}
				
				if (!existCommChannels)
				{
					CommChannelVo voCommChannel = rowComm.getValue();
					voCommChannel.setChannelType(rowComm.getcolType());
					voCommChannel.setCommValue(rowComm.getcolContactValue());
					voCommChannelColl.add(voCommChannel);
				}
			}
		}
		return voCommChannelColl.size() != 0 ? voCommChannelColl : null;	
	}

	private LocSiteVo populateSurgeryData(LocSiteVo value)
	{
		value.setAddress(populateAddressData(AddressType.SURGERY));  // WDEV-15936 specify address type 
		value.setName(form.ctnDetails().txtDetailsName().getValue());		
		value.setIsActive(new Boolean(form.ctnDetails().chkDetailsActive().getValue()));
		value.setCodeMappings(getCodeMappings());
		value.setType(LocationType.SURGERY);
		value.setCommChannels(populateCommChannelsFromScreen());
		
		return value;
	}
	
	private PersonAddress populateAddressData(AddressType addrType)
	{
		PersonAddress voAddress = new PersonAddress();		
		
		if (form.ctnDetails().txtDetailsAddress1().getValue() != null)	
			voAddress.setLine1(form.ctnDetails().txtDetailsAddress1().getValue());
		if (form.ctnDetails().txtDetailsAddress2().getValue() != null)
			voAddress.setLine2(form.ctnDetails().txtDetailsAddress2().getValue());
		if (form.ctnDetails().txtDetailsAddress3().getValue() != null)
			voAddress.setLine3(form.ctnDetails().txtDetailsAddress3().getValue());
		if (form.ctnDetails().txtDetailsAddress4().getValue() != null)
			voAddress.setLine4(form.ctnDetails().txtDetailsAddress4().getValue());
		if (form.ctnDetails().txtDetailsAddress5().getValue() != null)
			voAddress.setLine5(form.ctnDetails().txtDetailsAddress5().getValue());
		
		voAddress.setPostCode(form.ctnDetails().txtDetailsPostCode().getValue());
		voAddress.setCounty(form.ctnDetails().cmbCounty().getValue());
		voAddress.setAddressType(addrType);  // WDEV-15936 Address type should always be set

		return voAddress;
	}
	
	private TaxonomyMapCollection getCodeMappings()
	{
		TaxonomyMapCollection coll = new TaxonomyMapCollection();
		for (int i = 0; i < form.ctnDetails().grdDetailsMappings().getRows().size(); i++)
		{
			grdDetailsMappingsRow row = form.ctnDetails().grdDetailsMappings().getRows().get(i);
			if (row.getcolExtCodeType() != null && row.getcolCode() != null)
			{
				TaxonomyMap map = new TaxonomyMap();
				map.setTaxonomyName(row.getcolExtCodeType());
				map.setTaxonomyCode(row.getcolCode());
				coll.add(map);
			}
		}
		return coll;
	}

	protected void onBtnSaveClick() throws ims.framework.exceptions.PresentationLogicException
	{
		PracticeSearch practiceSearch = getMenuOption();
		if(practiceSearch == null)
			throw new CodingRuntimeException("No Menu Option Selected");
		
		if(practiceSearch.equals(PracticeSearch.NEWPRACTICE) || practiceSearch.equals(PracticeSearch.EDITPRACTICE))
		{
			if(savePractice())
			{
				if (isPracticeAdminDialog())
				{
					engine.close(DialogResult.OK);
				}

				if (practiceSearch.equals(PracticeSearch.NEWPRACTICE))
					form.getLocalContext().setSurgery(null);
				
				open();
				//WDEV-15663
				//form.customControlPracticeAdmin().setValue(form.getLocalContext().getPractice());
			}					
		}
		else if(practiceSearch.equals(PracticeSearch.NEWSURGERY) || practiceSearch.equals(PracticeSearch.EDITSURGERY))
		{
			if(saveSurgery())
			{
				if (isPracticeAdminDialog())
				{
					engine.close(DialogResult.OK);
				}
				
				open();
				//WDEV-15663
				//form.customControlPracticeAdmin().setValue(form.getLocalContext().getSurgery());
			}
		}
	}

	/**
	 * This functions sets up a practice for save and calls doSavePractice
	 * @return boolean indicating success (true)
	 */
	private boolean savePractice()
	{
		OrganisationVo practice = new OrganisationVo();
		
		if (form.getLocalContext().getPracticeIsNotNull())
			practice = form.getLocalContext().getPractice();
		practice = populatePracticeData(practice);
		
		practice.setType(OrganisationType.GPP);
		
		// this code adds a surgery to the value object if USE_GP_SURGERIES is false and if one doesn't already exist 
		// added to allow the ntpf to work as if using only practices. 
		if ( !practice.getLocationSitesIsNotNull())
		{				
			if (!canUseSurgery())
				practice = createOneMatchingChildSurgery(practice);
		}
		
		return doSavePractice(practice);
	}

	private boolean canUseSurgery() 
	{
		return true;//ConfigFlag.DOM.GP_USE_SURGERIES.getValue();
	}

	/**
	 * This functions attempts to save a practice. If successful, sets the saved practice as the local context
	 * @return boolean indicating success (true)
	 */
	private boolean doSavePractice(OrganisationVo practice) 
	{
		String[] errors = practice.validate();
		if(errors != null && errors.length > 0)
		{
			engine.showErrors(errors);
			return false;
		}
		
		try
		{
			practice = domain.saveOrg(practice);
			setPracticeToContext(practice);
			form.getLocalContext().setSurgery(null);
		}
		catch(StaleObjectException e)
		{
			if(e.getStaleObject() ==null)
			{
				setPracticeToContext(null);
				form.customControlPracticeAdmin().setValue(null);
				form.customControlPracticeAdmin().search();
			}
			
			engine.showMessage(ConfigFlag.UI.STALE_OBJECT_MESSAGE.getValue());
			open();
			updateControlState();
			return false;
		}
		catch (UniqueKeyViolationException e)
		{
			engine.showMessage(e.getMessage());
			return false;
		}
		
		return true;
	}
	
	/**
	 * creates a LocSiteCollection with one LocSiteVo. point is to create a 1 to 1 mapping 
	 * of practice - surgery for sites who are not visibly using surgeries. 
	 * 
	 * @param practice
	 * @return practice with 1 surgery, populated from the screen (matches practice values)
	 */
	private OrganisationVo createOneMatchingChildSurgery(OrganisationVo practice) 
	{
		LocSiteVoCollection surgeryCollection = new LocSiteVoCollection();
		LocSiteVo surgery = new LocSiteVo();
		surgery = populateSurgeryData(surgery);
		surgery.setName(surgery.getName() + " surgery");
		surgery.setCodeMappings(null);  // do not want to be creating duplicate location mappings. 
		surgery.setIsVirtual(false);  //wdev-4093 
		surgeryCollection.add(surgery);
		surgeryCollection.get(0).setParentOrganisation(practice);
		practice.setLocationSites(surgeryCollection);
			
		return practice;
	}
	
	/**
	 * This functions attempts to save a surgery
	 * @return boolean indicating success (true)
	 */
	private boolean saveSurgery() //throws UniqueKeyViolationException
	{
		OrganisationVo practice = form.getLocalContext().getPractice();
		if(practice == null)
		{
			engine.showErrors(new String[]{"Please select a Practice before saving a Surgery"});
			return false;
		}
		
		OrganisationVo practiceBeforeSave = practice != null?(OrganisationVo)practice.clone():null;
		
		LocSiteVoCollection locSiteCollection = new LocSiteVoCollection();			
		if (practice.getLocationSitesIsNotNull())
			locSiteCollection = (LocSiteVoCollection) practice.getLocationSites().clone();	
		
		LocSiteVo surgery = null;
		if(form.getLocalContext().getSurgeryIsNotNull())
			surgery = (LocSiteVo) form.getLocalContext().getSurgery().clone();
		if(surgery == null)
		{
			surgery = new LocSiteVo();
			surgery.setIsVirtual(Boolean.FALSE);
			surgery.setParentOrganisation(practice);
		}
		
		surgery = populateSurgeryData(surgery);
		form.getLocalContext().setSurgery(surgery);
		
		int surgeryIndex = locSiteCollection.indexOf(surgery);
		if(surgeryIndex < 0)
			locSiteCollection.add(surgery);
		else
			locSiteCollection.set(surgeryIndex, surgery);
		
		practice.setLocationSites(locSiteCollection);
		
		String[] errors = practice.validate();
		if(errors != null && errors.length > 0)
		{
			engine.showErrors(errors);
			practice.getLocationSites().remove(surgery);
			return false;
		}
		try
		{
			practice = domain.saveOrg(practice);
			surgery = getSavedSurgery(practiceBeforeSave, practice, surgery);
			
			setPracticeToContext(practice);
			form.getLocalContext().setSurgery(surgery);
		}
		catch(StaleObjectException e)
		{
			if (e.getStaleObject() == null)
			{
				form.getLocalContext().setSurgery(null);
				
				
				
			}
			engine.showMessage(ConfigFlag.UI.STALE_OBJECT_MESSAGE.getValue());
			clearInstanceControls();
			open();
			updateControlState();
			return false;
		}
		catch(UniqueKeyViolationException e)
		{
			engine.showMessage(e.getMessage());
			setPracticeToContext(practiceBeforeSave);
			return false;
		}
		
		return true;
	}

	private LocSiteVo getSavedSurgery(OrganisationVo practiceBeforeSave, OrganisationVo practice, LocSiteVo surgery)
	{
		if(practice == null)
			return null;
		
		int index = practice.getLocationSites().indexOf(surgery);
		if (index >= 0)
			return practice.getLocationSites().get(index);

		//Find the Location that's not in the practiceBeforeSave
		if(practiceBeforeSave != null && practiceBeforeSave.getLocationSitesIsNotNull())
		{
			for (int i = 0; i < practice.getLocationSites().size(); i++)
			{
				if(practiceBeforeSave.getLocationSites().indexOf(practice.getLocationSites().get(i)) < 0)
				{
					return practice.getLocationSites().get(i);
				}
			}
		}
		
		return null;
	}

	protected void onBtnCancelClick() throws PresentationLogicException 
	{
		open();
	}

	protected void onContextMenuItemClick(int menuItemID, ims.framework.Control sender) throws ims.framework.exceptions.PresentationLogicException
	{
		switch (menuItemID)
		{
			case GenForm.ContextMenus.GenericGrid.Add :
				form.ctnDetails().grdDetailsMappings().getRows().newRow();
			break;

			case GenForm.ContextMenus.AdminNamespace.PracticeCommChannels.ADD:
				newCommChannel();
				break;
				
			case GenForm.ContextMenus.AdminNamespace.PracticeCommChannels.REMOVE:
				removeCommChannel();
				break;
				
			case GenForm.ContextMenus.GenericGrid.Remove:
				removeTaxonomy();
				break;
				
			default :
			break;
		}
		updateControlState();
	}

	private void removeCommChannel() 
	{
		int index = form.ctnDetails().grdCommChannels().getSelectedRowIndex();
		if (index < 0)
		{
			engine.showMessage("Please select a Contact Type to remove");
			return;
		}
		form.ctnDetails().grdCommChannels().getRows().remove(index);
	}
	private void removeTaxonomy() 
	{
		int index = form.ctnDetails().grdDetailsMappings().getSelectedRowIndex();
		if (index < 0)
		{
			engine.showMessage("Please select a Contact Type to remove");
			return;
		}
		form.ctnDetails().grdDetailsMappings().getRows().remove(index);
	}

	private void newCommChannel() 
	{
		grdCommChannelsRow rowComm = form.ctnDetails().grdCommChannels().getRows().newRow();
		rowComm.setValue(new CommChannelVo());
	}

	protected void onBtnUpdateClick() throws ims.framework.exceptions.PresentationLogicException
	{
		if(form.customControlPracticeAdmin().getSelectedPractice() != null)
		{
			editPractice();
		}
		else if(form.customControlPracticeAdmin().getSelectedSurgery() != null)
		{
			editSurgery();
		}
	}	
	
	private void editSurgery() 
	{
		if (form.getLocalContext().getPracticeIsNotNull())
		{
			setMenuOption(PracticeSearch.EDITSURGERY);
			setDetailsContainerName(SURGERY_DETAILS);
			updateInstance();
		}
	}

	private void editPractice() 
	{
		setMenuOption(PracticeSearch.EDITPRACTICE);
		setDetailsContainerName(PRACTICE_DETAILS);
		updateInstance();
	}
	
	private void updateInstance() 
	{
		form.ctnDetails().setCollapsed(false);
		form.setMode(FormMode.EDIT);
	}

	protected void onBtnNewClick() throws ims.framework.exceptions.PresentationLogicException
	{
		if (form.customControlPracticeAdmin().getSelectedSurgery() != null)
		{
			newSurgery();
		}
		else
		{
			newPractice();
		}
	}
	
	private void newSurgery() 
	{
		//TODO - Add here support to add new surgery... - create an event or something...
		if (form.getLocalContext().getPracticeIsNotNull())  // cannot create a new surgery with no parent
		{
			form.getLocalContext().setSurgery(null);
			form.customControlPracticeAdmin().setValue(form.getLocalContext().getPractice());
			setMenuOption(PracticeSearch.NEWSURGERY);
			setDetailsContainerName(NEW_SURGERY);
			newInstance();
		}
	}

	private void newPractice() 
	{
		setPracticeToContext(null);
		form.customControlPracticeAdmin().setValue(null);
		setMenuOption(PracticeSearch.NEWPRACTICE);
		setDetailsContainerName(NEW_PRACTICE);
		newInstance();
	}
	
	private void newInstance()
	{
		clearInstanceControls();
		form.ctnDetails().txtDetailsName().setFocus();
		form.ctnDetails().setCollapsed(false);
		form.ctnDetails().chkDetailsActive().setValue(true);
		form.setMode(FormMode.EDIT);
	}

	protected void onBtnCloseClick() throws PresentationLogicException 
	{
		//WDEV-16516 
		if (isPracticeAdminDialog()&& engine.getPreviosFormName().equals(form.getForms().Admin.GPPracticeSelect))
		{	
		engine.close(DialogResult.CANCEL);
		}
		else
		{
		engine.close(DialogResult.OK);	
		}
	}

	private boolean removePractice()
	{
		OrganisationWithSitesVo voOrgWithSites = form.customControlPracticeAdmin().getSelectedPractice();
		if(voOrgWithSites == null)
			return false; 

		try
		{
			OrganisationVo voOrgganisation = domain.getOrg(voOrgWithSites);
			if(canUseSurgery())
			{	
				//WDEV-12000 if !null
				if(voOrgganisation!=null)
					domain.deletePractice(voOrgganisation);
			}
			else
			{
				//Delete the Surgery first, then the Practice 
				//WDEV-12000 if !null 
				if(voOrgganisation!=null)
					domain.deletePractice(voOrgganisation, Boolean.TRUE);
			}
			
			form.getLocalContext().setSurgery(null);
			setPracticeToContext(null);
			form.customControlPracticeAdmin().removePracticeFromGrid(voOrgWithSites);
			open();
		}
		catch (ForeignKeyViolationException excp) 
		{
			engine.showMessage(getGpListMessage(voOrgWithSites, excp));
			return false;
		}
		
		return true;
	}

	private String getGpListMessage(OrganisationWithSitesVo voOrgWithSites, ForeignKeyViolationException excp)
	{
		StringBuffer message = new StringBuffer();
		String[] gps = domain.listGps(voOrgWithSites);
		if(gps != null && gps.length > 0)
		{
			message.append("Cannot delete '" + voOrgWithSites.getName() + "' as it is currently associated with");
			message.append(" the following GPs:");
			for (int i = 0; i < gps.length; i++)
			{
				message.append(System.getProperty("line.separator"));
				message.append(gps[i]);
			}
		}
		else
		{
			message.append(excp.getMessage());
		}
		
		return message.toString();
	}

	private String getGpListMessageLoc(LocSiteVo locVo, ForeignKeyViolationException excp)
	{
		StringBuffer message = new StringBuffer();
		String[] gps = domain.listPatients(locVo);
		if(gps != null)
		{
			message.append("Cannot delete '" + locVo.getName() + "' as it is currently associated with");
			message.append(" " +gps.length + " Patients");

		}
		else
		{
			message.append(excp.getMessage());
		}
		
		return message.toString();
	}
	
	private boolean removeSurgery()
	{
		LocSiteVo locVo = form.customControlPracticeAdmin().getSelectedSurgery();
		if(locVo == null)
			return false;
		try
		{
			setPracticeToContext(domain.deleteLocSite(locVo));
			form.getLocalContext().setSurgery(null);
			form.customControlPracticeAdmin().removeSurgeryFromGrid(locVo);
			if(canUseSurgery())
				open();
		}
		catch (ForeignKeyViolationException e)
		{
			engine.showMessage(getGpListMessageLoc(locVo,e)); //wdev-11819
			return false;
		}
		
		return true;
	}
	
	protected void onCustomControlPracticeAdminValueChanged() throws PresentationLogicException
	{
		PracticeSearch val = form.customControlPracticeAdmin().getActionEvent();
		
		setMenuOption(val);
	
		if(val != null)
		{
			if(val.equals( PracticeSearch.SELECT))
			{
				onGrdResultsListSelectionChanged();
			}
			else if(val.equals(PracticeSearch.SEARCH))
			{
				clearInstanceControls();
				updateControlState();
				form.ctnDetails().setCaption(PRACTICE_DETAILS);		//wdev-16543
			}
			else if(val.equals( PracticeSearch.CLEAR))
			{
				clear();
				updateControlState();
				form.ctnDetails().setCaption(PRACTICE_DETAILS);		//wdev-16543
			}	
			else if(val.equals(PracticeSearch.NEWPRACTICE))
			{
				newPractice();
			}
			else if(val.equals(PracticeSearch.EDITPRACTICE))
			{
				onBtnUpdateClick();
			}
			else if(val.equals(PracticeSearch.NEWSURGERY))
			{
				newSurgery();
			}
			else if(val.equals(PracticeSearch.EDITSURGERY))
			{
				onBtnUpdateClick();
			}
			else if(val.equals(PracticeSearch.REMOVEPRACTICE))
			{
				removePractice();
			}
			else if(val.equals(PracticeSearch.REMOVESURGERY))
			{
				removeSurgery();
			}
			else if(val.equals(PracticeSearch.PRACTICEREMOVED))
			{
				clearInstanceControls();
				updateControlState();
			}
			else if(val.equals(PracticeSearch.SURGERYREMOVED))
			{
				clearInstanceControls();
				updateControlState();
			}
		}
		form.customControlPracticeAdmin().setActionEventHandled();
	}
	
	private PracticeSearch getMenuOption()
	{
		return form.getLocalContext().getMenuOption();
	}
	
	private void setMenuOption(PracticeSearch practiceSearch)
	{
		form.getLocalContext().setMenuOption(practiceSearch);
	}
	
	private boolean isPracticeAdminDialog()
	{
		return engine.getFormName().equals(form.getForms().Admin.PracticeAdminDialog);
	}
	
	private void setDetailsContainerName(String name)
	{
		form.ctnDetails().setCaption(name);
	}
	
	private void setPracticeToContext(OrganisationVo voOrganisation)
	{
		form.getLocalContext().setPractice(voOrganisation);
		
		if (isPracticeAdminDialog())
			form.getGlobalContext().Admin.setPractice(voOrganisation);
	}
	
	private Boolean isRemoveVisible() 
	{
		if(form.getGlobalContext().Admin.getInvisibleRemoveContextMenuIsNotNull())
		{
			boolean isInvisible = form.getGlobalContext().Admin.getInvisibleRemoveContextMenu().booleanValue();
			if(isInvisible)
				return Boolean.FALSE;
		}
			
		return Boolean.TRUE;
	}

	@Override
	protected void onGrdCommChannelsSelectionChanged() throws PresentationLogicException 
	{
		updateControlState();
	}

	
	protected void onGrdDetailsMappingsSelectionChanged()
			throws PresentationLogicException {
		
		updateControlState();
	}

	


}
