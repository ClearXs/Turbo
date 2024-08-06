package cc.allio.turbo.modules.office.documentserver.managers.history;

import cc.allio.turbo.modules.office.documentserver.models.filemodel.Document;

// specify the history manager functions
public interface HistoryManager {

    String[] getHistory(Document document);  // get document history
}
