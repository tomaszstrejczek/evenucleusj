package com.beimin.eveapi.character.mail.messages;

import com.beimin.eveapi.core.AbstractListParser;
import com.beimin.eveapi.core.ApiAuth;
import com.beimin.eveapi.core.ApiPage;
import com.beimin.eveapi.core.ApiPath;
import com.beimin.eveapi.exception.ApiException;

public class MailMessagesParser extends AbstractListParser<MailMessagesHandler, MailMessagesResponse, ApiMailMessage> {
	private MailMessagesParser() {
		super(MailMessagesResponse.class, 2, ApiPath.CHARACTER, ApiPage.MAIL_MESSAGES, MailMessagesHandler.class);
	}

	public static MailMessagesParser getInstance() {
		return new MailMessagesParser();
	}

	@Override
	public MailMessagesResponse getResponse(ApiAuth<?> auth) throws ApiException {
		return super.getResponse(auth);
	}
}