package online.ccbync.spear;

import java.util.function.Function;

interface Handler extends Function<RequestContext, Void> {
}
