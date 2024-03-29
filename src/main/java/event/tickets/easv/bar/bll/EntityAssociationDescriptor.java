package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.dal.database.EntityAssociation;

public record EntityAssociationDescriptor<A, B>(Class<A> entityA, Class<B> entityB,
                                                EntityAssociation<A, B> entityAssociation) {}