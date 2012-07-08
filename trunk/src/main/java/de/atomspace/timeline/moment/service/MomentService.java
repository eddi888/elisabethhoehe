package de.atomspace.timeline.moment.service;

import java.util.ArrayList;
import java.util.List;

import org.ektorp.AttachmentInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.atomspace.timeline.moment.domain.Moment;

@Service
public class MomentService {

	@Autowired
	MomentRepository momentRepository;

	public void add(Moment moment) {
		momentRepository.add(moment);
	}

	public void update(Moment moment) {
		momentRepository.update(moment);
	}

	public AttachmentInputStream getAttachment(String momentId, String attachmentId) {
		AttachmentInputStream data = momentRepository.getDb().getAttachment(momentId, attachmentId);
		return data;
	}

	public Moment findOneById(String id) {
		return momentRepository.get(id);
	}
	public List<Moment> findByYear(int year) {
		List<Moment> all = momentRepository.findByYear(year);
		List<Moment> byYears = new ArrayList<Moment>();
		for (Moment moment : all) {
			if (moment.isPublished() == true) {
				byYears.add(moment);
			}
		}

		return byYears;
	}

	public List<Moment> findByYear(int year, String visibleKey) {
		List<Moment> all = momentRepository.findByYear(year);
		List<Moment> byYears = new ArrayList<Moment>();
		for (Moment moment : all) {
			if (moment.isPublished() == true || visibleKey.equals(moment.getVisibleKey())) {
				byYears.add(moment);
			}
		}

		return byYears;
	}

}
