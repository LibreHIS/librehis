// This code was generated by Cristian Belciug using IMS Development Environment (version 1.80 build 4050.19540)
// Copyright (C) 1995-2011 IMS MAXIMS. All rights reserved.

package ims.RefMan.forms.clinicalnotedialog;

import ims.RefMan.vo.AppointmentClinicalNotesVo;
import ims.RefMan.vo.AppointmentClinicalNotesVoCollection;
import ims.RefMan.vo.ReferralClinicalNotesVo;
import ims.RefMan.vo.lookups.ClinicalNoteTypeForAnAppointment;
import ims.RefMan.vo.lookups.ReportNoteStatus;
import ims.RefMan.vo.lookups.ReportNoteType;
import ims.configuration.AppRight;
import ims.configuration.gen.ConfigFlag;
import ims.domain.exceptions.StaleObjectException;
import ims.framework.MessageButtons;
import ims.framework.MessageIcon;
import ims.framework.enumerations.DialogResult;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.DateTime;

public class Logic extends BaseLogic
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void onFormOpen(Object[] args) throws ims.framework.exceptions.PresentationLogicException
	{
		initialize();//WDEV-14006
		open();
	}

	//WDEV-14006
	private void initialize()
	{
		boolean isAuthoringEnabled = form.getGlobalContext().RefMan.getAppointmentClinicalNote().getID_AppointmentClinicalNotes() == null;
		
		form.ccAuthoringInfo().initializeComponent();
		form.ccAuthoringInfo().setEnabledAuthoringHCP(isAuthoringEnabled);
		form.ccAuthoringInfo().setEnabledDateTime(isAuthoringEnabled);
		form.ccAuthoringInfo().setIsRequiredPropertyToControls(true);
		bindcmbStatusLookup();
		showCorrespondenceNotesControls(false);
	}

	private void bindcmbStatusLookup()
	{
		form.cmbStatus().clear();
		ims.RefMan.vo.lookups.ReportNoteStatusCollection lookupCollection = ims.RefMan.vo.lookups.LookupHelper.getReportNoteStatus(this.domain.getLookupService());
		for(int x = 0; x < lookupCollection.size(); x++)
		{
			if (!lookupCollection.get(x).equals(ReportNoteStatus.PREVIEW)) {
				form.cmbStatus().newRow(lookupCollection.get(x), lookupCollection.get(x).getText(), lookupCollection.get(x).getImage(), lookupCollection.get(x).getTextColor());
			}
		}
	}

	private void open()
	{
		populateScreenFromData();
		updateControlsState();
	}

	private void updateControlsState()
	{
		form.cmbNoteType().setEnabled((form.getGlobalContext().RefMan.getAppointmentClinicalNoteIsNotNull() 
				&& form.getGlobalContext().RefMan.getAppointmentClinicalNote().getID_AppointmentClinicalNotes() == null)
				|| ( engine.hasRight(AppRight.CAN_EDIT_ALL_CLINICAL_NOTES)) );
	}

	private void populateScreenFromData()
	{
		form.ccAuthoringInfo().setValue(form.getGlobalContext().RefMan.getAppointmentClinicalNote().getAuthoringInformation());//WDEV-14006
		form.cmbNoteType().setValue(form.getGlobalContext().RefMan.getAppointmentClinicalNote().getClinicalNoteType());
		form.txtNote().setValue(form.getGlobalContext().RefMan.getAppointmentClinicalNote().getClinicalNote());
		
		if (form.getGlobalContext().RefMan.getAppointmentClinicalNote().getClinicalNoteTypeIsNotNull()
			&& form.getGlobalContext().RefMan.getAppointmentClinicalNote().getClinicalNoteType().equals(ClinicalNoteTypeForAnAppointment.CORRESPONDENCE_NOTES))
		{
			showCorrespondenceNotesControls(true);
			form.cmbReportNoteType().setValue(form.getGlobalContext().RefMan.getAppointmentClinicalNote().getNoteType());
			form.cmbStatus().setValue(form.getGlobalContext().RefMan.getAppointmentClinicalNote().getNoteStatus());
			form.chkGP().setValue(form.getGlobalContext().RefMan.getAppointmentClinicalNote().getCopyToGP());
			form.chkPatient().setValue(form.getGlobalContext().RefMan.getAppointmentClinicalNote().getCopyToPatient());
			form.chkSecCare().setValue(form.getGlobalContext().RefMan.getAppointmentClinicalNote().getCopyToSecondaryCare());
		}
	}

	@Override
	protected void onBtnCancelClick() throws PresentationLogicException
	{
		engine.close(DialogResult.CANCEL);
	}

	@Override
	protected void onBtnOkClick() throws PresentationLogicException
	{
		if (form.cmbNoteType().getValue() != null 
			&& form.cmbNoteType().getValue().equals(ClinicalNoteTypeForAnAppointment.CORRESPONDENCE_NOTES))
		{
			StringBuffer sb = new StringBuffer();
			
			if (form.cmbReportNoteType().getValue() == null)
				sb.append("Correspondence Notes Type is mandatory.\n\n");

			if (form.cmbStatus().getValue() == null)
				sb.append("Status is mandatory.");
			
			if (form.chkGP().getValue() == Boolean.FALSE
				&& form.chkPatient().getValue() == Boolean.FALSE
				&& form.chkSecCare().getValue() == Boolean.FALSE)
				sb.append("Please select at least one of the Copy To checkboxes and save again.");

			if (sb.length() > 0)
			{
				engine.showMessage(sb.toString(),"Missing Data",MessageButtons.OK, MessageIcon.WARNING);
				return;
			}
		}
		
		if(save())
			engine.close(DialogResult.OK);
	}

	private boolean save()
	{
		form.getLocalContext().setshouldRemoveLC(false);
		if (engine.hasRight(AppRight.CAN_EDIT_ALL_CLINICAL_NOTES)
			&& form.cmbNoteType().getValue() != null 
			&& !form.cmbNoteType().getValue().equals(ClinicalNoteTypeForAnAppointment.CORRESPONDENCE_NOTES)
			&& form.getGlobalContext().RefMan.getAppointmentClinicalNote().getClinicalNoteTypeIsNotNull()
			&& form.getGlobalContext().RefMan.getAppointmentClinicalNote().getClinicalNoteType().equals(ClinicalNoteTypeForAnAppointment.CORRESPONDENCE_NOTES))
		{
			//If the note is not null and Status equals draft
			if (form.getGlobalContext().RefMan.getAppointmentClinicalNote().getID_AppointmentClinicalNotesIsNotNull())
			{
				AppointmentClinicalNotesVoCollection currentNotes = domain.listClinicalNotes(form.getGlobalContext().RefMan.getCatsReferral());		

				form.getLocalContext().setshouldRemoveLC(!isOtherNoteOfThisTypeActive(form.getGlobalContext().RefMan.getAppointmentClinicalNote(), currentNotes));
				
				//Type of note has change, and no other active note with this type
				if (form.getGlobalContext().RefMan.getAppointmentClinicalNoteIsNotNull()
					&& !isOtherNoteOfThisTypeActive(form.getGlobalContext().RefMan.getAppointmentClinicalNote(), currentNotes))
				{
					form.getLocalContext().setremoveTypeLC(form.getGlobalContext().RefMan.getAppointmentClinicalNote().getNoteType());
				}
			}
	
		}
		
		AppointmentClinicalNotesVo clinicalNote = populateDataFromScreen();
		
		if(clinicalNote.getID_AppointmentClinicalNotes() == null)
		{
			return addNote(clinicalNote);
		}
		else
		{
			return editNote(clinicalNote);
		}
	}

	private boolean editNote(AppointmentClinicalNotesVo clinicalNote)
	{
		String[] errors = clinicalNote.validate();
		
		if(errors != null && errors.length > 0)
		{
			engine.showErrors(errors);
			return false;
		}
		
		ReportNoteType removeType = null;
		boolean shouldRemove = false;
		//If the note is not null and Status equals draft
		if (clinicalNote.getID_AppointmentClinicalNotesIsNotNull())
		{
			AppointmentClinicalNotesVoCollection currentNotes = domain.listClinicalNotes(form.getGlobalContext().RefMan.getCatsReferral());		

			if (ReportNoteStatus.DRAFT.equals(clinicalNote.getNoteStatus()))
				shouldRemove = !isOtherNoteOfThisTypeActive(clinicalNote, currentNotes);
			
			//Type of note has change, and no other active note with this type
			if (form.getGlobalContext().RefMan.getAppointmentClinicalNoteIsNotNull()
				&& (form.getGlobalContext().RefMan.getAppointmentClinicalNote().getNoteTypeIsNotNull()
						&& !form.getGlobalContext().RefMan.getAppointmentClinicalNote().getNoteType().equals(clinicalNote.getNoteType())) 
				&& !isOtherNoteOfThisTypeActive(clinicalNote, currentNotes))
			{
				removeType = clinicalNote.getNoteType();
			}
		}
		
		if (engine.hasRight(AppRight.CAN_EDIT_ALL_CLINICAL_NOTES))
		{
			shouldRemove = form.getLocalContext().getshouldRemoveLC();
			removeType = form.getLocalContext().getremoveTypeLC();
		}
		try
		{
			form.getGlobalContext().RefMan.setAppointmentClinicalNote(domain.saveNote(clinicalNote, shouldRemove, removeType));
		}
		catch (StaleObjectException e)
		{
			e.printStackTrace();
			engine.showMessage(ConfigFlag.UI.STALE_OBJECT_MESSAGE.getValue());
			engine.close(DialogResult.ABORT);
			return false;
		}
		
		return true;
	}

	private boolean addNote(AppointmentClinicalNotesVo clinicalNote)
	{
		ReferralClinicalNotesVo referralClinicalNotes = populateReferralClinicalNotes();
		
		String[] errors = referralClinicalNotes.validate(clinicalNote.validate());
		if(errors != null && errors.length > 0)
		{
			engine.showErrors(errors);
			return false;
		}
		
		ReportNoteType removeType = null;
		boolean shouldRemove = false;
		//If the note is not null and Status equals draft
		if (clinicalNote.getID_AppointmentClinicalNotesIsNotNull())
		{
			AppointmentClinicalNotesVoCollection currentNotes = domain.listClinicalNotes(form.getGlobalContext().RefMan.getCatsReferral());		

			if (ReportNoteStatus.DRAFT.equals(clinicalNote.getNoteStatus()))
				shouldRemove = !isOtherNoteOfThisTypeActive(clinicalNote, currentNotes);
			
			//Type of note has change, and no other active note with this type
			if (form.getGlobalContext().RefMan.getAppointmentClinicalNoteIsNotNull()
				&& (form.getGlobalContext().RefMan.getAppointmentClinicalNote().getNoteTypeIsNotNull()
						&& !form.getGlobalContext().RefMan.getAppointmentClinicalNote().getNoteType().equals(clinicalNote.getNoteType())) 
				&& !isOtherNoteOfThisTypeActive(clinicalNote, currentNotes))
			{
				removeType = clinicalNote.getNoteType();
			}
		}

		try
		{
			form.getGlobalContext().RefMan.setAppointmentClinicalNote(domain.addClinicalNote(referralClinicalNotes, clinicalNote, shouldRemove, removeType));
		}
		catch (StaleObjectException e)
		{
			e.printStackTrace();
			engine.showMessage(ConfigFlag.UI.STALE_OBJECT_MESSAGE.getValue());
			engine.close(DialogResult.ABORT);
			return false;
		}
		
		//wdev-12880
		if(form.getGlobalContext().RefMan.getCatsReferral() != null)
			form.getGlobalContext().RefMan.setCatsReferral(domain.getCatsReferral(form.getGlobalContext().RefMan.getCatsReferral()));

		return true;
	}

	private boolean isOtherNoteOfThisTypeActive(AppointmentClinicalNotesVo note, AppointmentClinicalNotesVoCollection currentNotes)
	{
		if (note == null)
			return false;
			
		for (int i = 0 ; currentNotes != null && i < currentNotes.size() ; i++)
		{
			if (note.getNoteType().equals(currentNotes.get(i).getNoteType()) 
				&& ReportNoteStatus.ACTIVE.equals(currentNotes.get(i).getNoteStatus()) 
				&& !note.equals(currentNotes.get(i)))
				return true;	
		}
		return false;
	}

	private ReferralClinicalNotesVo populateReferralClinicalNotes()
	{
		ReferralClinicalNotesVo referralClinicalNotes = form.getGlobalContext().RefMan.getReferralClinicalNotes();
		
		if(referralClinicalNotes == null)
			referralClinicalNotes = new ReferralClinicalNotesVo();
		
		if(referralClinicalNotes.getCatsReferral() == null)
			referralClinicalNotes.setCatsReferral(form.getGlobalContext().RefMan.getCatsReferral());
		
		if(referralClinicalNotes.getClinicalNotes() == null)
			referralClinicalNotes.setClinicalNotes(new AppointmentClinicalNotesVoCollection());
		
		return referralClinicalNotes;
	}

	private AppointmentClinicalNotesVo populateDataFromScreen()
	{
		AppointmentClinicalNotesVo clinicalNotes = form.getGlobalContext().RefMan.getAppointmentClinicalNote();
		
		clinicalNotes.setAuthoringInformation(form.ccAuthoringInfo().getValue());//WDEV-14006
		clinicalNotes.setClinicalNoteType(form.cmbNoteType().getValue());
		clinicalNotes.setClinicalNote(form.txtNote().getValue());
		
		if (form.cmbNoteType().getValue() != null
			&& form.cmbNoteType().getValue().equals(ClinicalNoteTypeForAnAppointment.CORRESPONDENCE_NOTES))
		{
			clinicalNotes.setNoteType(form.cmbReportNoteType().getValue());
			clinicalNotes.setNoteStatus(form.cmbStatus().getValue());
			clinicalNotes.setCopyToGP(form.chkGP().getValue());
			clinicalNotes.setCopyToPatient(form.chkPatient().getValue());
			clinicalNotes.setCopyToSecondaryCare(form.chkSecCare().getValue());
			clinicalNotes.setCorrespondenceMadeActiveDateTime(null);
			if (form.cmbStatus().getValue() != null
				&& form.cmbStatus().getValue().equals(ReportNoteStatus.ACTIVE))
				clinicalNotes.setCorrespondenceMadeActiveDateTime(new DateTime());
		}
		else
		{
			clinicalNotes.setNoteType(null);
			clinicalNotes.setNoteStatus(null);
			clinicalNotes.setCopyToGP(null);
			clinicalNotes.setCopyToPatient(null);
			clinicalNotes.setCopyToSecondaryCare(null);
			clinicalNotes.setCorrespondenceMadeActiveDateTime(null);
		}
		return clinicalNotes;
	}

	@Override
	//WDEV-14027 
	protected void onImbSpellcheckClick() throws PresentationLogicException
	{
		form.txtNote().spellCheck();
		
	}

	@Override
	protected void onCmbNoteTypeValueChanged() throws PresentationLogicException
	{
		showCorrespondenceNotesControls(false);
		if (form.cmbNoteType().getValue() != null
			&& form.cmbNoteType().getValue().equals(ClinicalNoteTypeForAnAppointment.CORRESPONDENCE_NOTES))
			showCorrespondenceNotesControls(true);
	}

	private void showCorrespondenceNotesControls(boolean bShow)
	{
		form.lblCorr1().setVisible(bShow);
		form.lblCorr2().setVisible(bShow);
		form.lblCorr3().setVisible(bShow);
		form.cmbReportNoteType().setVisible(bShow);
		form.cmbStatus().setVisible(bShow);
		form.chkGP().setVisible(bShow);
		form.chkPatient().setVisible(bShow);
		form.chkSecCare().setVisible(bShow);

		if (! bShow)
		{
			form.cmbReportNoteType().setValue(null);
			form.cmbStatus().setValue(null);
			form.chkGP().setValue(null);
			form.chkPatient().setValue(null);
			form.chkSecCare().setValue(null);
		}
	}
}