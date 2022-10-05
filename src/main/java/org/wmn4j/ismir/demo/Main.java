package org.wmn4j.ismir.demo;

import org.wmn4j.analysis.harmony.KSKeyAnalysis;
import org.wmn4j.io.ParsingFailureException;
import org.wmn4j.io.ScoreReader;
import org.wmn4j.io.musicxml.MusicXmlReader;
import org.wmn4j.mir.Pattern;
import org.wmn4j.mir.PatternBuilder;
import org.wmn4j.mir.search.PointSetSearch;
import org.wmn4j.mir.search.Search;
import org.wmn4j.notation.ChordBuilder;
import org.wmn4j.notation.Durations;
import org.wmn4j.notation.NoteBuilder;
import org.wmn4j.notation.Pitch;
import org.wmn4j.notation.Score;
import org.wmn4j.notation.access.Selection;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	/**
	 * Expects one argument: the path to a score in MusicXML format.
	 *
	 * @param args the first argument should be the path to a score in MusicXML format.
	 */
	public static void main(String[] args) {
		final Path inputPath = Paths.get(args[0]);

		// 1. Reading a score from MusicXML.
		// Score reader classes implement Java's Closable-interface,
		// so they can be used with the try-with-resources pattern that
		// ensures streams are closed.
		try (ScoreReader reader = MusicXmlReader.readerFor(inputPath)) {
			final Score score = reader.readScore();

			// Create an executor for parallel execution of searches and analyses.
			final ExecutorService executor = Executors.newCachedThreadPool();

			// 2. Search the score for a pattern
			// Create a searchable version of the score using point-set based
			// search that works with polyphonic music and queries.
			final Search searchableScore = PointSetSearch.of(score);

			// Create two queries and submit them to the executor.
			final var searchResultA = executor.submit(() -> searchableScore.findOccurrences(getQueryA()));
			final var searchResultB = executor.submit(() -> searchableScore.findOccurrences(getQueryB()));

			// 3. Run key analysis (using Krumhansl-Schmuckler algorithm) of the first
			// and second half of the score.
			// Create two selections of the score.
			final var firstMeasureNumber = score.hasPickupMeasure() ? 0 : 1;
			final var lastMeasureNumber = score.getFullMeasureCount();
			final Selection firstHalf = score.selectRange(firstMeasureNumber, lastMeasureNumber / 2);
			final Selection secondHalf = score.selectRange((lastMeasureNumber / 2) + 1, lastMeasureNumber);

			// Submit the analysis tasks to the executor.
			final var firstSelectionKey = executor.submit(() -> KSKeyAnalysis.of(firstHalf).getKey());
			final var secondSelectionKey = executor.submit(() -> KSKeyAnalysis.of(secondHalf).getKey());

			// Print the results.
			System.out.println("Found " + searchResultA.get().size() + " occurrences of pattern A");
			System.out.println("Found " + searchResultB.get().size() + " occurrences of pattern B");
			System.out.println("Key of first half  : " + firstSelectionKey.get());
			System.out.println("Key of second half : " + secondSelectionKey.get());

			executor.shutdown();
		} catch (IOException | ParsingFailureException | InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	private static Pattern getQueryA() {
		// Basic outline of the "Ode to Joy" theme's beginning.
		return new PatternBuilder()
				.add(new NoteBuilder(Pitch.of(Pitch.Base.E, Pitch.Accidental.NATURAL, 4), Durations.QUARTER))
				.add(new NoteBuilder(Pitch.of(Pitch.Base.E, Pitch.Accidental.NATURAL, 4), Durations.QUARTER))
				.add(new NoteBuilder(Pitch.of(Pitch.Base.F, Pitch.Accidental.NATURAL, 4), Durations.QUARTER))
				.add(new NoteBuilder(Pitch.of(Pitch.Base.G, Pitch.Accidental.NATURAL, 4), Durations.QUARTER))
				.add(new NoteBuilder(Pitch.of(Pitch.Base.G, Pitch.Accidental.NATURAL, 4), Durations.QUARTER))
				.add(new NoteBuilder(Pitch.of(Pitch.Base.F, Pitch.Accidental.NATURAL, 4), Durations.QUARTER))
				.add(new NoteBuilder(Pitch.of(Pitch.Base.E, Pitch.Accidental.NATURAL, 4), Durations.QUARTER))
				.add(new NoteBuilder(Pitch.of(Pitch.Base.D, Pitch.Accidental.NATURAL, 4), Durations.QUARTER))
				.build();
	}

	private static Pattern getQueryB() {
		// A major triad in adjacent thirds
		return new PatternBuilder()
				.add(new ChordBuilder(
						new NoteBuilder(Pitch.of(Pitch.Base.C, Pitch.Accidental.NATURAL, 4), Durations.QUARTER))
						.add(new NoteBuilder(Pitch.of(Pitch.Base.E, Pitch.Accidental.NATURAL, 4), Durations.QUARTER))
						.add(new NoteBuilder(Pitch.of(Pitch.Base.G, Pitch.Accidental.NATURAL, 4), Durations.QUARTER)))
				.build();
	}
}
