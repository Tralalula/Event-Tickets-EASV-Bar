package event.tickets.easv.bar.dal.database;

public interface AssociationSQLTemplate<A, B> {
    String insertRelationSQL();
    String deleteRelationSQL();
    String selectAForBSQL();
    String selectBForASQL();
    String deleteAssociationsForASQL();
    String deleteAssociationsForBSQL();
}
