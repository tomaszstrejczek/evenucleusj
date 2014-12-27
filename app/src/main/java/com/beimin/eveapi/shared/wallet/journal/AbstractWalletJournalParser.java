package com.beimin.eveapi.shared.wallet.journal;

import java.util.HashMap;
import java.util.Map;

import com.beimin.eveapi.core.AbstractListParser;
import com.beimin.eveapi.core.ApiAuth;
import com.beimin.eveapi.core.ApiPage;
import com.beimin.eveapi.core.ApiPath;
import com.beimin.eveapi.exception.ApiException;

public abstract class AbstractWalletJournalParser extends
		AbstractListParser<WalletJournalHandler, WalletJournalResponse, ApiJournalEntry> {
	public AbstractWalletJournalParser(ApiPath path) {
		super(WalletJournalResponse.class, 2, path, ApiPage.WALLET_JOURNAL, WalletJournalHandler.class);
	}

	public WalletJournalResponse getResponse(ApiAuth<?> auth, int accountKey) throws ApiException {
		return getResponse(auth, "accountKey", Integer.toString(accountKey));
	}

	public WalletJournalResponse getResponse(ApiAuth<?> auth, int accountKey, long beforeRefID, int rowCount)
			throws ApiException {
		Map<String, String> extraParams = new HashMap<String, String>();
		extraParams.put("accountKey", Integer.toString(accountKey));
		extraParams.put("rowCount", Integer.toString(rowCount));
		extraParams.put("beforeRefID", Long.toString(beforeRefID));
		return getResponse(auth, extraParams);
	}
}