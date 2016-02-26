/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// --------------------------------------------------------------------------*/

/**
 * Standard C Header.
 *
 * @since 2016/02/26
 */

/** Header guard. */
#ifndef SJME_hGSYS_STDCH
#define SJME_hGSYS_STDCH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXSYS_STDCH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/** Configuration Header. */
#include "config.h"

/** Standard C systems only. */
#if SJME_TARGET == SJME_TARGET_CSTANDARD || defined(SJME_STANDARD_C)

/****************************************************************************/

/** Define standard C just in case. */
#ifndef SJME_STANDARD_C
	#define SJME_STANDARD_C
#endif

/** Include standard library stuff. */
#include "stdlib.h"

/** Where code is placed. */
#ifndef sjme_code
	#define sjme_code
#endif

/** Where normal memory is used. */
#ifndef sjme_mem
	#define sjme_mem
#endif

/** Strings in the code area. */
#ifndef sjme_codestr
	#define sjme_codestr(v) v
#endif

/** Strings in the memory area. */
#ifndef sjme_memstr
	#define sjme_memstr(v) v
#endif

/** Do not modify variables. */
#ifndef sjme_const
	#define sjme_const const
#endif

/****************************************************************************/

/** End. */
#endif

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXSYS_STDCH
}
#undef SJME_cXSYS_STDCH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXSYS_STDCH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGSYS_STDCH */

