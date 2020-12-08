#@namespace scala modulex.domain.thrift

enum TResult {
    SUCCESS = 0
    FAILED
}

struct TRedirectResult{
    1: i16 statusCode
    2: TResult result
}